package com.sofamaniac.crabir.ui.post.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.data.NetworkPolicy
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
    showHidden: Boolean = false,
    isMostVisible: Boolean,
    viewModel: PostViewModelInterface = koinViewModel<LinkViewModel>(key = post.id) {
        parametersOf(
            post
        )
    },
) {
    val postOpt by viewModel.post.collectAsState(post)
    if (postOpt == null) {
        ThemedCard(modifier = Modifier.height(100.dp)) {}
    } else {
        val post = postOpt!!
        if (!showHidden && post.relationship.hidden) {
            return
        }
        PostCardContent(
            post,
            modifier,
            interactions = viewModel,
            isMostVisible = isMostVisible,
        )
    }
}

@Composable
internal fun PostCardContent(
    post: PostData,
    modifier: Modifier = Modifier,
    isMostVisible: Boolean = false,
    interactions: LinkInteraction,
) {

    val viewSettings = LocalViewSettings.current
    val context = LocalContext.current
    val connectionState = context.onWifiConnection
    val videoSettings = LocalDataSettings.current.videoQuality
    val canStartVideo = when (videoSettings.autostart) {
        NetworkPolicy.Always -> isMostVisible
        NetworkPolicy.OnWifi -> connectionState && isMostVisible
        else -> false
    } // We do not apply the padding on the column, but on each of its children except
    // the body to have images that take the full width
    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val navController = LocalNavController.current
    val openPost = {
        navController?.navigate(PostRoute(post.permalink)) ?: Unit
    }
    val showOpenButton = LocalPostSettings.current.buttonsSettings.comments
    ThemedCard(
        roundedCorners = viewSettings.cardSettings.roundedCorners,
        modifier = modifier.fillMaxWidth(),
        onClick = {
            navController?.navigate(PostRoute(post.permalink)) ?: Unit
        }
    ) {
        PostHeader(
            post,
            showSubredditIcon = viewSettings.cardSettings.showSubredditIcon,
            modifier = innerModifier.padding(vertical = 8.dp),
            showPrefix = viewSettings.prefixCommunity
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        val likes by interactions.likes.collectAsState()
        val read by interactions.read.collectAsState()
        PostInfo(
            post,
            modifier = innerModifier,
            enableThumbnail = enablePreview && viewSettings.cardSettings.thumbnailForLinkPreview,
            likes = likes,
            read = read,
        )
        PostBody(
            post,
            canPlayVideo = canStartVideo,
            imageHeight = viewSettings.cardSettings.imageHeight,
            enableTextPreview = viewSettings.cardSettings.enableTextPreview && !post.spoiler,
            maxLines = viewSettings.cardSettings.maxLines,
            enableLinkFullSizePreview = !viewSettings.cardSettings.thumbnailForLinkPreview,
        )
        BottomRow(
            post,
            innerModifier,
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
            isMostVisible = false,
            interactions = viewModel,
        )
    }
}

