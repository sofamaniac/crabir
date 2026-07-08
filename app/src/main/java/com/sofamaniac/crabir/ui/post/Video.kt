/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality
import com.sofamaniac.crabir.navigation.FullscreenVideoRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.theme.GIF_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.VIDEO_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.YOUTUBE_CARTOUCHE_COLOR
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.crabirBlurStyle
import com.sofamaniac.crabir.ui.media.FullscreenBottomBar
import com.sofamaniac.crabir.ui.media.FullscreenTopBar
import com.sofamaniac.crabir.ui.media.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.image.DownloadButton
import com.sofamaniac.crabir.ui.media.image.ImageView
import com.sofamaniac.crabir.ui.media.videoPlayer.DecoratedVideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(UnstableApi::class)
@Composable
fun PostVideo(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
    blur: Boolean = false,
    goFullscreen: (Route) -> Unit
) {
    val video = getVideoUrl(post)
    val placeholder =
        @Composable {
            val image = post.getObfuscated()
            if (image != null && blur) {
                AsyncImage(
                    image.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                val blurStyle = crabirBlurStyle()
                val placeholderModifier = Modifier.hazeEffect {
                    inputScale = HazeInputScale.Fixed(0.5f)
                    blurEffect {
                        style = blurStyle
                        blurEnabled = blur
                    }
                }
                ImageView(
                    post = post,
                    modifier = placeholderModifier.fillMaxSize(),
                    allowTransformation = false,
                    contentScale = ContentScale.FillBounds,
                    quality = Quality.Medium,
                )
            }
        }
    val uriHandler = LocalUriHandler.current

    if (video == null) {
        val host = post.url.toUri().host
        val domain = host?.removePrefix("www.")?.split(".")?.firstOrNull()
        val isGif = post.url.toUri().lastPathSegment?.endsWith(".gif") ?: false
        val text = if (isGif) "GIF" else domain ?: "Video"
        val cartoucheColor = if (isGif) GIF_CARTOUCHE_COLOR else VIDEO_CARTOUCHE_COLOR
        Box(modifier = modifier.clickable {
            uriHandler.openUri(post.url)
        }) {
            placeholder()
            Text(
                text,
                modifier = Modifier
                    .padding(8.dp)
                    .cartouche(backgroundColor = cartoucheColor)
                    .align(Alignment.TopEnd)
            )
        }
    } else {
        val goFullscreen = {
            goFullscreen(FullscreenVideoRoute(post.name))
        }
        PostVideo(
            video,
            key = post.id,
            canPlayVideo,
            blur,
            modifier = modifier,
            goFullscreen = goFullscreen,
            placeholder = placeholder,
        )
    }
}

@Composable
fun PostVideo(
    video: MediaResource,
    key: String,
    canPlayVideo: Boolean,
    blur: Boolean,
    modifier: Modifier = Modifier,
    goFullscreen: () -> Unit,
    placeholder: @Composable () -> Unit,
) {
    DecoratedVideoPlayer(
        video,
        key = key,
        autostart = canPlayVideo && !blur,
        placeholder = placeholder,
        clickable = !blur,
        modifier = modifier.clickable(enabled = blur) {
            goFullscreen()
        },
        fullscreenButton = {
            IconButton(onClick = {
                goFullscreen()
            }) {
                Icon(
                    Icons.Default.Fullscreen,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun YoutubeVideo(post: PostData, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = {
                uriHandler.openUri(post.url)
            })
    ) {
        ImageView(
            post,
            quality = Quality.High,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize(),
            allowTransformation = false
        )
        Text(
            "Youtube",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .cartouche(YOUTUBE_CARTOUCHE_COLOR)
        )
    }
}


fun getVideoUrl(post: PostData): MediaResource? {
    var media =
        post.media.media?.redditVideo?.toMediaResource() ?: post.crosspostParentList.firstOrNull()
            ?.let { getVideoUrl(it) }
    media = media ?: post.preview?.images?.firstOrNull()?.variants?.mp4?.source?.toMediaResource()
    media = media ?: post.preview?.redditVideoPreview?.toMediaResource()
    return media
}

@Composable
fun FullscreenVideo(
    post: Fullname,
    viewModel: PostDataViewModel = koinViewModel { parametersOf(post) },
    dismiss: () -> Unit
) {
    val post = viewModel.post.collectAsState(initial = null).value ?: return
    var showDecorations by remember { mutableStateOf(true) }
    val video = getVideoUrl(post)!!
    VerticalSwipeToDismiss(
        topBar = {
            FullscreenTopBar(showDecorations, actions = { DownloadButton(video.url.toUri()) })
        },
        bottomBar = {
            FullscreenBottomBar(post, showDecorations) {
                PlayerControls {
                    IconButton(onClick = dismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.FullscreenExit,
                            contentDescription = "Exit fullscreen",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
        onDismiss = dismiss,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showDecorations = !showDecorations
            },
    ) {
        VideoPlayer(
            video,
            key = post.id,
            startPlaying = true,
            mute = false,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}