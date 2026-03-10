/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:08 PM
 *
 */

package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.domain.model.Kind
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.repository.VotableRepository
import com.sofamaniac.reboost.ui.markdown.RedditMarkdown
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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


@HiltViewModel(assistedFactory = VotableViewModel.Factory::class)
class VotableViewModel @AssistedInject constructor(
    @Assisted val id: String,
    private val posts: VotableRepository,
) : ViewModel() {

    val likes = posts.observePost(id).map { it?.relationship?.liked }
    val saved = posts.observePost(id).map { it?.relationship?.saved ?: false }

    fun upvote() {
        viewModelScope.launch {
            posts.upvote(id)
        }
    }

    fun downvote() {
        viewModelScope.launch {
            posts.downvote(id)
        }
    }

    fun save(target: Boolean) {
        viewModelScope.launch {
            if (target) {
                posts.save(id)
            } else {
                posts.unsave(id)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(postId: String): VotableViewModel
    }

}
