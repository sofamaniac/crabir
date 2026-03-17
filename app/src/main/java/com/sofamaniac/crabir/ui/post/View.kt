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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    val selftextView = @Composable {
        val selftext = post.selftext.markdown
        RedditMarkdown(
            markdown = selftext,
            maxLines = maxLines,
            modifier = modifier.padding(horizontal = 16.dp),
            mediaMetadata = post.mediaMetadata
        )
    }
    when (post.kind) {
        Kind.Image -> {
            PostImage(post, modifier.fillMaxWidth())
        }

        Kind.Video -> {
            PostVideo(post, modifier.fillMaxWidth(), canPlayVideo = canPlayVideo)
        }

        Kind.Link -> {
            if (enableLinkFullSizePreview) {
                PostImage(post, modifier.fillMaxWidth(), enabled = false)
            }
        }

        Kind.Gallery -> {
            PostGallery(
                post,
                modifier.fillMaxWidth(),
                canPlayVideo = canPlayVideo
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
    fun upvote()
    fun downvote()
    fun save(target: Boolean)

    fun hide() {}
    fun unhide() {}
}


@HiltViewModel(assistedFactory = VotableViewModel.Factory::class)
class VotableViewModel @AssistedInject constructor(
    @Assisted val id: String,
    private val posts: VotableRepository,
) : ViewModel(), VotableInteraction {

    override val likes = posts.observePost(id).map { it?.relationship?.liked }
    override val saved = posts.observePost(id).map { it?.relationship?.saved ?: false }

    override fun upvote() {
        viewModelScope.launch {
            posts.upvote(id)
        }
    }

    override fun downvote() {
        viewModelScope.launch {
            posts.downvote(id)
        }
    }

    override fun save(target: Boolean) {
        viewModelScope.launch {
            if (target) {
                posts.save(id)
            } else {
                posts.unsave(id)
            }
        }
    }

    override fun hide() {
        viewModelScope.launch {
            val id = if (id.startsWith("t3")) id else "t3_$id"
            posts.hide(id)
        }
    }

    override fun unhide() {
        viewModelScope.launch {
            val id = if (id.startsWith("t3")) id else "t3_$id"
            posts.unhide(id)
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(postId: String): VotableViewModel
    }

}
