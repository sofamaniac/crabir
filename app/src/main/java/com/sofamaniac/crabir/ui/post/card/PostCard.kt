package com.sofamaniac.crabir.ui.post.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.data.NetworkPolicy
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.post.BottomRow
import com.sofamaniac.crabir.ui.post.DummyInteraction
import com.sofamaniac.crabir.ui.post.LinkInteraction
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import com.sofamaniac.crabir.ui.post.PostViewModelInterface
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    showHidden: Boolean = false,
    dim: Boolean = false,
    isMostVisible: Boolean,
    viewModel: PostViewModelInterface = koinViewModel<LinkViewModel>(key = post.id) {
        parametersOf(
            post
        )
    },
) {
    val postOpt by viewModel.post.collectAsState(post)
    if (postOpt == null) {
        ThemedCard(
            modifier = Modifier.height(100.dp),
            elevation = CardDefaults.elevatedCardElevation(),
            onClick = onClick,
        ) {}
    } else {
        val post = postOpt!!
        if (!showHidden && post.relationship.hidden) {
            return
        }
        PostCardContent(
            post,
            modifier,
            interactions = viewModel,
            dim = dim,
            isMostVisible = isMostVisible,
            onClick = onClick,
        )
    }
}

@Composable
internal fun PostCardContent(
    post: PostData,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isMostVisible: Boolean = false,
    dim: Boolean = false,
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
    }
    // We do not apply the padding on the column, but on each of its children except
    // the body to have images that take the full width
    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    ThemedCard(
        roundedCorners = viewSettings.cardSettings.roundedCorners,
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
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
            dim = dim && read,
        )
        BottomRow(
            post,
            interactions = interactions,
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

