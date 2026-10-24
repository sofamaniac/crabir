package com.sofamaniac.crabir.ui.media.videoPlayer

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.ContentFrame
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.onWifiConnection


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
    val modifier = modifier
        .fillMaxSize()
        .aspectRatio(media.aspectRatio)
        .clipToBounds()

    VideoPlayer(media.url, key, modifier, placeholder, startPlaying, mute)
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    url: String,
    key: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {},
    startPlaying: Boolean = false,
    mute: Boolean = true,
) {
    val player = remember { VideoPlayerManager.getInstance() }
    val currentKey by VideoPlayerManager.currentKey.collectAsState()
    val isActive = currentKey == key

    val dataSettings = LocalDataSettings.current
    val context = LocalContext.current
    val currentQuality = if (context.onWifiConnection) {
        dataSettings.videoQuality.onWifi
    } else {
        dataSettings.videoQuality.onMobile
    }

    LaunchedEffect(startPlaying) {
        if (startPlaying) {
            VideoPlayerManager.setMediaItem(
                url,
                key,
                playWhenReady = true,
                volume = if (mute) 0f else 1f,
                quality = currentQuality
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
    ) {

        placeholder()
        if (isActive) {
            ContentFrame(
                player = player,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                shutter = { placeholder() }
            )
        }
    }
}
