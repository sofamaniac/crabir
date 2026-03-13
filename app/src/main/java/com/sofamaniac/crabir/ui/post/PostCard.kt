package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.ThemedCard

/**
 * Composable function that displays a single post in a Card format.
 *
 * This function creates a view for a given [Post], including its header,
 * content, and bottom row of actions.
 *
 * @param post The [com.sofamaniac.crabir.domain.model.PostData] data to display.
 * @param modifier Modifier for the root layout of the post.
 * @param enableThumbnail Whether to enable the thumbnail preview. Defaults to true. The thumbnail is shown only if there is one and the post if a link.
 * @param showSubredditIcon Whether to display the subreddit icon in the header. Defaults to true.
 * @param clickable Whether the post is clickable to navigate to the thread view. Defaults to true.
 * @param onClick A lambda that takes a [com.sofamaniac.crabir.domain.model.PostData] and is called before navigating to the post.
 * @param canStartVideo Whether the post can start a video. Defaults to false.
 */
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    onClick: (PostData) -> Unit = {},
    canStartVideo: Boolean = false,
    read: Boolean = false,
    viewModel: VotableViewModel = hiltViewModel<VotableViewModel, VotableViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post.id)
        }),
) {
    val settings = rememberViewSettings()
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
    ThemedCard(
        shape = RoundedCornerShape(0),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClickCard,
    ) {
        PostHeader(
            post,
            showSubredditIcon = settings.cardSettings.showSubredditIcon,
            modifier = modifier.padding(vertical = 8.dp),
            showPrefix = settings.prefixCommunity
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = enablePreview && settings.cardSettings.thumbnailForLinkPreview,
            viewModel = viewModel,
            read = read,
        )
        PostBody(
            post,
            canPlayVideo = canStartVideo,
            enableFullHeightImage = settings.cardSettings.enableFullHeightImage,
            enableTextPreview = settings.cardSettings.enableTextPreview,
            maxLines = settings.cardSettings.maxLines,
            enableLinkFullSizePreview = !settings.cardSettings.thumbnailForLinkPreview
        )
        BottomRow(post, modifier, viewModel = viewModel) {
            IconButton(onClick = { onClick(post) }) {
                Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = "Go to comments")
            }
        }
    }
}