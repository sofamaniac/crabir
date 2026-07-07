package com.sofamaniac.crabir.ui.post.card

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.post.AutoPlayVideo
import com.sofamaniac.crabir.settings.post.rememberPostsSettings
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.DummyInteraction
import com.sofamaniac.crabir.ui.post.LinkInteraction
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.PostViewModelInterface
import com.sofamaniac.crabir.ui.post.buttons.OpenThreadButton
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable function that displays a single post in a Card format.
 *
 * This function creates a view for a given [Post], including its header,
 * content, and bottom row of actions.
 *
 * @param post The [com.sofamaniac.crabir.domain.model.PostData] data to display.
 * @param modifier Modifier for the root layout of the post.
 * @param clickable Whether the post is clickable to navigate to the thread view. Defaults to true.
 * @param markAsRead A lambda that takes a [com.sofamaniac.crabir.domain.model.PostData] and is called before navigating to the post.
 * @param canStartVideo Whether the post can start a video. Defaults to false.
 */
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    markAsRead: () -> Unit = {},
    read: Boolean = false,
    showHidden: Boolean = false,
    isMostVisible: Boolean,
    viewModel: PostViewModelInterface = koinViewModel<LinkViewModel>(key = post.id) {
        parametersOf(
            post
        )
    },
) {
    val postOpt by viewModel.post.collectAsState(post)
    if (postOpt == null) return
    val post = postOpt!!
    if (!showHidden && post.relationship.hidden) {
        return
    }
    PostCardContent(
        post,
        modifier,
        clickable,
        markAsRead,
        read = read,
        interactions = viewModel,
        isMostVisible = isMostVisible,
    )
}

//@Composable
//internal fun canStartVideo(): Boolean {
//    val autoplay = rememberPostsSettings().linksSettings.autoPlayVideos
//    if (autoplay == AutoPlayVideo.Never) {
//        return false
//    }
//    val context = LocalContext.current
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val netInfo = connectivityManager.allNetworks
//}
//
@Composable
internal fun PostCardContent(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    markAsRead: () -> Unit = {},
    isMostVisible: Boolean = false,
    read: Boolean = false,
    interactions: LinkInteraction,
) {

    val viewSettings = rememberViewSettings()
    val context = LocalContext.current
    val connectionState = context.onWifiConnection
    val settings by interactions.linksSettings.collectAsState(initial = null)
    val canStartVideo = when (settings?.autoPlayVideos) {
        AutoPlayVideo.Always -> true
        AutoPlayVideo.Wifi -> connectionState && isMostVisible
        else -> false
    }
    Log.d("PostCard", "canStartVideo: $canStartVideo")
    // We do not apply the padding on the column, but on each of its children except []
    // to have images that take the full width
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val navController = LocalNavController.current
    val openPost = if (clickable) {
        {
            markAsRead()
            navController?.navigate(PostRoute(post.permalink)) ?: Unit
        }
    } else {
        {}
    }
    val showOpenButton = rememberPostsSettings().buttonsSettings.comments
    val markdownState by interactions.markdown.collectAsState()
    ThemedCard(
        roundedCorners = viewSettings.cardSettings.roundedCorners,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = openPost,
    ) {
        PostHeader(
            post,
            showSubredditIcon = viewSettings.cardSettings.showSubredditIcon,
            modifier = modifier.padding(vertical = 8.dp),
            showPrefix = viewSettings.prefixCommunity
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        val likes by interactions.likes.collectAsState(post.relationship.liked)
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = enablePreview && viewSettings.cardSettings.thumbnailForLinkPreview,
            likes = likes,
            read = read,
            markAsRead = markAsRead
        )
        PostBody(
            post,
            canPlayVideo = canStartVideo,
            enableFullHeightImage = viewSettings.cardSettings.enableFullHeightImage,
            enableTextPreview = viewSettings.cardSettings.enableTextPreview && !post.spoiler,
            maxLines = viewSettings.cardSettings.maxLines,
            enableLinkFullSizePreview = !viewSettings.cardSettings.thumbnailForLinkPreview,
            markAsRead = markAsRead,
            markdownState = markdownState,
        )
        BottomRow(
            post,
            modifier,
            interactions = interactions,
            action = if (!showOpenButton) null else {
                {
                    OpenThreadButton(openPost)
                }
            }
        )

    }
}

@Preview
@Composable
internal fun PostCardPreview() {
    val viewModel: DummyInteraction = viewModel()
    val post by viewModel.post.collectAsState()
    AnimatedVisibility(visible = true) {
        PostCardContent(
            post,
            clickable = false,
            markAsRead = {},
            isMostVisible = false,
            read = false,
            interactions = viewModel,
        )
    }
}

