/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:08 PM
 *
 */

package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.LocalTheme
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
internal fun PostBody(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
) {
    when (post.kind) {
        Kind.Image -> {
            PostImage(post, modifier.fillMaxWidth())
        }

        Kind.Video -> {
            PostVideo(post, modifier.fillMaxWidth(), canPlayVideo = canPlayVideo)
        }

        Kind.Link -> {
            // TODO check if there is a preview, if not show the link
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
            if (selftext.isNotBlank()) {
                RedditMarkdown(
                    markdown = selftext,
                    maxLines = 6,
                    modifier = modifier.padding(horizontal = 16.dp),
                    mediaMetadata = post.mediaMetadata
                )
            }
        }
    }
}


/**
 * Composable function that displays a single post in a Card format.
 *
 * This function creates a view for a given [Post], including its header,
 * content, and bottom row of actions.
 *
 * @param post The [PostData] data to display.
 * @param modifier Modifier for the root layout of the post.
 * @param enableThumbnail Whether to enable the thumbnail preview. Defaults to true. The thumbnail is shown only if there is one and the post if a link.
 * @param showSubredditIcon Whether to display the subreddit icon in the header. Defaults to true.
 * @param clickable Whether the post is clickable to navigate to the thread view. Defaults to true.
 * @param onClick A lambda that takes a [PostData] and is called before navigating to the post.
 * @param body A composable lambda that defines the main content/body of the post (e.g., text, image). It should manage the horizontal padding itself
 */
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    enableThumbnail: Boolean = true,
    showSubredditIcon: Boolean = true,
    clickable: Boolean = true,
    onClick: (PostData) -> Unit = {},
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post.id)
        }),
    body: @Composable () -> Unit,
) {
    // We do not apply the padding on the column, but on each of its children except [body]
    // to have images that take the full width
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val theme = LocalTheme.current
    val onClickCard = if (clickable) {
        { onClick(post) }
    } else {
        {}
    }
    Card(
        shape = RoundedCornerShape(0),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClickCard,
        colors = CardDefaults.cardColors().copy(containerColor = theme.cardBackground)
    ) {
        PostHeader(
            post,
            showSubredditIcon = showSubredditIcon,
            modifier = modifier.padding(vertical = 8.dp)
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = enablePreview && enableThumbnail,
            viewModel = viewModel,
        )
        body()
        BottomRow(post, modifier, visitPost = onClick, viewModel = viewModel)
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
