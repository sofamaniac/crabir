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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.rememberFiltersSettings
import com.sofamaniac.crabir.ui.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.media.image.ImageView
import com.sofamaniac.crabir.ui.media.videoPlayer.DecoratedVideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls

@OptIn(UnstableApi::class)
@Composable
fun PostVideo(post: PostData, modifier: Modifier = Modifier, canPlayVideo: Boolean = false) {
    val video = getVideoUrl(post)
    val filters = rememberFiltersSettings()
    val blur = post.spoiler || (post.over18 && filters.blurNSFW)
    val placeholderModifier = if (blur) Modifier.blur(40.dp) else Modifier
    val placeholder =
        @Composable {
            ImageView(
                post,
                allowTransformation = false,
                modifier = placeholderModifier.fillMaxSize()
            )
        }
    val uriHandler = LocalUriHandler.current
    val fullscreenManager = LocalFullscreenHandler.current!!

    if (video == null) {
        val host = post.url.toUri().host
        val domain = host?.removePrefix("www.")?.split(".")?.firstOrNull()
        Box(modifier = Modifier.clickable {
            uriHandler.openUri(post.url)
        }) {
            placeholder()
            Text(
                domain ?: "Video",
                modifier = Modifier
                    .padding(8.dp)
                    .cartouche(backgroundColor = Color(64, 196, 255, 255))
                    .align(Alignment.TopEnd)
            )
        }
    } else {
        DecoratedVideoPlayer(
            video,
            startPlaying = canPlayVideo && !blur,
            placeholder = placeholder,
            clickable = !blur,
            modifier = modifier.clickable(enabled = blur) {
                fullscreenManager.push {
                    FullscreenVideo(post)
                }
            },
            fullscreenButton = {
                IconButton(onClick = {
                    fullscreenManager.push {
                        FullscreenVideo(post)
                    }
                }) {
                    Icon(
                        Icons.Default.Fullscreen,
                        contentDescription = "Go fullscreen",
                        tint = Color.White
                    )
                }
            }
        )
    }

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
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            allowTransformation = false
        )
        Text(
            "Youtube",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .cartouche(Color.Red)
        )
    }
}


fun getVideoUrl(post: PostData): MediaResource? {
    val media =
        post.media.media?.redditVideo?.toMediaResource() ?: post.crosspostParentList.firstOrNull()
            ?.let { getVideoUrl(it) }
    if (media != null) {
        return media
    }
    return post.preview?.images?.firstOrNull()?.variants?.mp4?.source?.toMediaResource()
}

@Composable
fun FullscreenVideo(post: PostData) {

    var showDecorations by remember { mutableStateOf(true) }
    val video = getVideoUrl(post)!!
    val fullscreenManager = LocalFullscreenHandler.current!!
    VerticalSwipeToDismiss(
        topBar = {
            FullscreenTopBar(showDecorations, actions = {})
        },
        bottomBar = {
            FullscreenBottomBar(post, showDecorations) {
                PlayerControls {
                    IconButton(onClick = {
                        fullscreenManager.pop()
                    }, modifier = Modifier.size(32.dp)) {
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
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showDecorations = !showDecorations
            },
    ) {
        VideoPlayer(
            video,
            startPlaying = true,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}