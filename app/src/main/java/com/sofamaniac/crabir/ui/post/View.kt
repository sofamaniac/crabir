/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:08 PM
 *
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@Composable
internal fun PostBody(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
    enableFullHeightImage: Boolean = true,
    enableTextPreview: Boolean = true,
    maxLines: Int?,
    enableLinkFullSizePreview: Boolean = true,
    forceShowSelftext: Boolean = false,
    markAsRead: () -> Unit = {},
) {
    val navController = LocalNavController.current!!
    fun goFullscreen(route: Route) {
        markAsRead()
        navController.navigate(route)
    }

    val selftextView = @Composable {
        val selftext = post.selftext.markdown
        RedditMarkdown(
            markdown = selftext,
            maxLines = maxLines,
            modifier = modifier.padding(horizontal = 16.dp),
            mediaMetadata = post.mediaMetadata,
            //onClick = { goFullscreen(PostRoute(post.permalink, null)) }
        )
    }
    when (post.kind) {
        Kind.Image -> {
            PostImage(
                post,
                modifier.fillMaxWidth(),
                goFullscreen = ::goFullscreen,
            )
        }

        Kind.Video -> {
            PostVideo(
                post,
                modifier.fillMaxWidth(),
                canPlayVideo = canPlayVideo,
                goFullscreen = ::goFullscreen
            )
        }

        Kind.Link -> {
            if (enableLinkFullSizePreview) {
                PostImage(
                    post,
                    modifier.fillMaxWidth(),
                    enabled = false,
                    goFullscreen = ::goFullscreen,
                )
            }
        }

        Kind.Gallery -> {
            PostGallery(
                post,
                modifier.fillMaxWidth(),
                canPlayVideo = canPlayVideo,
                goFullscreen = ::goFullscreen,
            )
        }

        Kind.YoutubeVideo -> {
            YoutubeVideo(
                post,
                modifier.fillMaxWidth(),
            )
        }

        Kind.Streamable -> {
            StreamableVideo(
                post,
                canPlayVideo = canPlayVideo,
                modifier = modifier.fillMaxWidth(),
                goFullscreen = ::goFullscreen
            )
        }

        else -> {
            val selftext = post.selftext.markdown
            if (selftext.isNotBlank() && enableTextPreview) {
                selftextView()
                return
            }
        }
    }
    if (forceShowSelftext) {
        val selftext = post.selftext.markdown
        if (selftext.isNotBlank() && enableTextPreview) {
            selftextView()
        }
    }
}

interface VotableInteraction {
    val likes: Flow<Boolean?>
    val saved: Flow<Boolean>
    val rules: StateFlow<Rules>
    fun upvote(name: Fullname)
    fun downvote(name: Fullname)
    fun save(name: Fullname, target: Boolean)
    fun fetchRules()
    fun report(reason: String)
}

interface LinkInteraction : VotableInteraction {
    val post: Flow<PostData>
    val flairs: StateFlow<List<FlairInfo>>
    fun hide()

    fun unhide()

    fun delete()

    fun editFlair(flairId: String, text: String?)

    fun getFlairs()

    fun markNSFW()

    fun unmarkNSFW()

    fun markSpoiler()

    fun unmarkSpoiler()

    fun setInboxReplies(enabled: Boolean)
}


open class VotableViewModel<T : VotableData>(
    val name: String,
    val subreddit: String,
    private val posts: VotableRepository<T>,
) : ViewModel(), VotableInteraction {

    val fullname =
        Fullname(name)

    init {
        require(name.contains("_"))
    }

    override val likes = posts.get(fullname).map { it?.relationship?.liked }
    override val saved = posts.get(fullname).map { it?.relationship?.saved ?: false }

    var _rules = MutableStateFlow(Rules())
    override val rules: StateFlow<Rules> = _rules

    override fun fetchRules() {
        if (_rules.value.rules.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            _rules.value = posts.getRules(subreddit)
        }
    }

    override fun report(reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.report(fullname, reason)
        }
    }

    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.upvote(fullname)
        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.downvote(fullname)
        }
    }

    override fun save(name: Fullname, target: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                posts.save(fullname)
            } else {
                posts.unsave(fullname)
            }
        }
    }
}


@HiltViewModel(assistedFactory = LinkViewModel.Factory::class)
open class LinkViewModel @AssistedInject constructor(
    @Assisted("post") post: PostData,
    private val posts: LinksRepository,
    private val history: VisitedPostsDao,
) : VotableViewModel<PostData>(post.name.name, post.subreddit.name, posts), LinkInteraction {

    override val post = posts.get(post.name).map { it ?: post }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
        initialValue = post
    )

    private var _flairs = MutableStateFlow(emptyList<FlairInfo>())
    override val flairs: StateFlow<List<FlairInfo>> = _flairs

    fun markPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity: VisitedPostEntity =
                VisitedPostEntity(post.name, System.currentTimeMillis(), visitedBy)
            history.insert(entity)
        }
    }

    override fun hide() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.hide(fullname)
        }
    }

    override fun unhide() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unhide(fullname)
        }
    }

    override fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.delete(fullname)
        }
    }

    override fun editFlair(flairId: String, text: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.editFlair(fullname, flairId, text)
        }
    }

    override fun getFlairs() {
        viewModelScope.launch(Dispatchers.IO) {
            _flairs.value = posts.getFlairs(fullname)
        }
    }

    override fun markNSFW() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.markNSFW(fullname)
        }
    }

    override fun unmarkNSFW() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unmarkNSFW(fullname)
        }
    }

    override fun markSpoiler() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.markSpoiler(fullname)
        }
    }

    override fun unmarkSpoiler() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unmarkSpoiler(fullname)
        }
    }

    override fun setInboxReplies(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.setInboxReplies(fullname, enabled)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("post") post: PostData
        ): LinkViewModel
    }
}