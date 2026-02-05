package com.sofamaniac.reboost.ui.media.videoPlayer

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.sofamaniac.reboost.domain.model.MediaResource
import com.sofamaniac.reboost.ui.Cartouche
import com.sofamaniac.reboost.ui.media.videoPlayer.controls.AlwaysOnInfo
import com.sofamaniac.reboost.ui.media.videoPlayer.controls.Controls


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    media: MediaResource,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = {
        Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
            Text("LOADING")
        }
    },
    cartouche: @Composable (() -> Unit)? = {
        Cartouche(backgroundColor = Color.Cyan) {
            Text("Gif")
        }
    },
    fullscreenButton: @Composable (() -> Unit)? = null,
    startPlaying: Boolean = false,
) {
    var showControls by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val player = remember { VideoPlayerManager.getInstance(context) }
    var hasFirstFrame by remember { mutableStateOf(true) }
    val currentUrl = VideoPlayerManager.currentUrl.collectAsState()

    LaunchedEffect(startPlaying, currentUrl) {
        Log.d("VideoPlayer", "launching effect")
        if (startPlaying) {
            VideoPlayerManager.setMediaItem(media.url)
            player.playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (currentUrl.value == media.url) {
                VideoPlayerManager.stopPlayer()
            }
        }
    }

    val playing = rememberPlayPauseButtonState(player).showPlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(media.aspectRatio)
            .clickable(onClick = { showControls = !showControls })
    ) {

        if (currentUrl.value == media.url) {
            ContentFrame(
                player = player,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        } else {
            placeholder?.invoke()
            if (!playing) {
                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    cartouche?.invoke()
                }
            }
        }

        if (showControls) {
            Controls(fullscreenButton = fullscreenButton)

        } else {
            AlwaysOnInfo(player, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}