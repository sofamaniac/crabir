package com.sofamaniac.crabir.ui.media.videoPlayer

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.state.rememberPresentationState
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.settings.theme.GIF_CARTOUCHE_COLOR
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.AlwaysOnInfo
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls


@OptIn(UnstableApi::class)
@Composable
fun DecoratedVideoPlayer(
    media: MediaResource,
    key: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {
        Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
            Text("LOADING")
        }
    },
    cartouche: @Composable (() -> Unit)? = {
        Text("Gif", modifier = Modifier.cartouche(GIF_CARTOUCHE_COLOR))
    },
    fullscreenButton: @Composable (() -> Unit)? = null,
    autostart: Boolean = false,
    startMuted: Boolean = true,
    clickable: Boolean = true,
) {
    var showControls by remember { mutableStateOf(false) }

    val player = remember { VideoPlayerManager.getInstance() }
    val currentKey by VideoPlayerManager.currentKey.collectAsState()
    val presentationState = rememberPresentationState(player)
    val isActive = currentKey == key
    val loading = isActive && presentationState.coverSurface

    val showDecoration = presentationState.coverSurface || !player.playWhenReady || !isActive

    LaunchedEffect(autostart) {
        Log.d("DecoratedVideoPlayer", "key: $key, startPlaying: $autostart")
        if (autostart) {
            VideoPlayerManager.setMediaItem(
                media.url,
                key,
                playWhenReady = true,
                volume = if (startMuted) 0f else 1f
            )
        } else if (isActive) {
            player.pause()
        }
    }

    @Composable
    fun BoxScope.loadDecoration() {
        if (showDecoration) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                cartouche?.invoke()
            }
        }
        if (loading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Color.Black.copy(alpha = 0.3f))
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }

    val modifier = modifier
        .fillMaxWidth()
        .aspectRatio(media.aspectRatio)
        .let { mod ->
            if (clickable) {
                mod.clickable {
                    if (!isActive) {
                        VideoPlayerManager.setMediaItem(media.url, key)
                        player.playWhenReady = true
                    } else {
                        showControls = !showControls
                    }
                }
            } else {
                mod
            }
        }
    Box(
        modifier = modifier
    ) {

        VideoPlayer(media, key, placeholder = placeholder, startPlaying = autostart)
        if (isActive && !presentationState.coverSurface) {
            if (showControls) {
                PlayerControls(
                    fullscreenButton = fullscreenButton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            } else {
                AlwaysOnInfo(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(all = 8.dp)
                )
            }
        } else {
            loadDecoration()
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    media: MediaResource,
    key: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {},
    startPlaying: Boolean = false,
    mute: Boolean = true,
) {
    val player = remember { VideoPlayerManager.getInstance() }
    val currentKey by VideoPlayerManager.currentKey.collectAsState()
    val isActive = currentKey == key


    LaunchedEffect(startPlaying) {
        if (startPlaying) {
            VideoPlayerManager.setMediaItem(
                media.url,
                key,
                playWhenReady = true,
                volume = if (mute) 0f else 1f
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(media.aspectRatio)
    ) {

        if (isActive) {
            ContentFrame(
                player = player,
                //surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                shutter = { placeholder() }
            )
        } else {
            placeholder()
        }
    }
}