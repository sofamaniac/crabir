/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:08 PM
 *
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.api.FlairInfo
import com.sofamaniac.crabir.data.remote.api.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@Composable
internal fun ColumnScope.PostBody(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
    enableFullHeightImage: Boolean = true,
    enableTextPreview: Boolean = true,
    maxLines: Int = 5,
    enableLinkFullSizePreview: Boolean = true,
    forceShowSelftext: Boolean = false,
) {
    val navController = LocalNavController.current!!
    fun goFullscreen(route: Route) {
        navController.navigate(route)
    }

    val selftextView = @Composable {
        val selftext = post.selftext.markdown
        RedditMarkdown(
            markdown = selftext,
            maxLines = maxLines,
            modifier = modifier.padding(horizontal = 16.dp),
            mediaMetadata = post.mediaMetadata,
            onClick = { goFullscreen(PostRoute(post.permalink, null)) }
        )
    }
    when (post.kind) {
        Kind.Image -> {
            PostImage(post, modifier.fillMaxWidth(), goFullscreen = { goFullscreen(it) })
        }

        Kind.Video -> {
            PostVideo(post, modifier.fillMaxWidth(), canPlayVideo = canPlayVideo)
        }

        Kind.Link -> {
            if (enableLinkFullSizePreview) {
                PostImage(
                    post,
                    modifier.fillMaxWidth(),
                    enabled = false,
                    goFullscreen = { goFullscreen(it) }
                )
            }
        }

        Kind.Gallery -> {
            PostGallery(
                post,
                modifier.fillMaxWidth(),
                canPlayVideo = canPlayVideo,
                goFullscreen = { goFullscreen(it) }
            )
        }

        Kind.YoutubeVideo -> {
            YoutubeVideo(
                post,
                modifier.fillMaxWidth(),
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
    fun upvote(name: Fullname)
    fun downvote(name: Fullname)
    fun save(name: Fullname, target: Boolean)
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

    var rules by mutableStateOf(Rules())

    fun getRules() {
        if (rules.rules.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            rules = posts.getRules(subreddit)
        }
    }

    fun report(reason: String) {
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


//    @AssistedFactory
//    interface Factory {
//        fun<T: VotableData> create(
//            // Because fullname is a value class it cannot be used in a factory
//            // https://github.com/google/dagger/issues/4613
//            @Assisted("fullname") fullname: String,
//            @Assisted("subreddit") subreddit: String
//        ): VotableViewModel<T>
//    }
//
}


@HiltViewModel(assistedFactory = LinkViewModel.Factory::class)
class LinkViewModel @AssistedInject constructor(
    @Assisted("post") post: PostData,
    private val posts: LinksRepository,
) : VotableViewModel<PostData>(post.name.name, post.subreddit.name, posts), VotableInteraction {

    val post = posts.get(post.name).stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
        initialValue = post
    )

    val flairs = mutableStateOf(emptyList<FlairInfo>())

    fun hide() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.hide(fullname)
        }
    }

    fun unhide() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unhide(fullname)
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.delete(fullname)
        }
    }

    fun editFlair(flairId: String, text: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            posts.editFlair(fullname, flairId, text)
        }
    }

    fun getFlairs() {
        viewModelScope.launch(Dispatchers.IO) {
            flairs.value = posts.getFlairs(fullname)
        }
    }

    fun markNSFW() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.markNSFW(fullname)
        }
    }

    fun unmarkNSFW() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unmarkNSFW(fullname)
        }
    }

    fun markSpoiler() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.markSpoiler(fullname)
        }
    }

    fun unmarkSpoiler() {
        viewModelScope.launch(Dispatchers.IO) {
            posts.unmarkSpoiler(fullname)
        }
    }

    fun setInboxReplies(enabled: Boolean) {
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
