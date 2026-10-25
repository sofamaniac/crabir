package com.sofamaniac.crabir.ui.media.videoPlayer

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.material3.buttons.MuteButton
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.state.rememberPresentationState
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.settings.data.VideoQuality
import com.sofamaniac.crabir.settings.theme.GIF_CARTOUCHE_COLOR
import com.sofamaniac.crabir.ui.components.cartouche
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.AlwaysOnInfo
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls

@OptIn(UnstableApi::class)
@Composable
fun DecoratedVideoPlayer(
    media: MediaResource,
    key: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    },
    cartouche: @Composable (() -> Unit)? = {
        Text(
            stringResource(R.string.gif_cartouche),
            modifier = Modifier.cartouche(colorResource(GIF_CARTOUCHE_COLOR))
        )
    },
    goFullscreen: (() -> Unit)? = null,
    autostart: Boolean = false,
    startMuted: Boolean = true,
    clickable: Boolean = true,
    quality: VideoQuality = VideoQuality.Auto,
) {
    var showControls by remember { mutableStateOf(false) }

    val player = remember { VideoPlayerManager.getInstance() }
    val currentKey by VideoPlayerManager.currentKey.collectAsState()
    val presentationState = rememberPresentationState(player)
    val isActive = currentKey == key
    val loading = isActive && presentationState.coverSurface

    val showDecoration = presentationState.coverSurface || !player.playWhenReady || !isActive

    fun start() {
        VideoPlayerManager.setMediaItem(
            media.url,
            key,
            playWhenReady = true,
            volume = if (startMuted) 0f else 1f,
            quality = quality,
        )
    }

    LaunchedEffect(autostart) {
        Log.d(
            "DecoratedVideoPlayer",
            "key: $key, startPlaying: $autostart, startMuted: $startMuted"
        )
        if (autostart) {
            start()
        } else if (isActive) {
            VideoPlayerManager.pause(key)
        }
    }

    val modifier = modifier
        .fillMaxWidth()
        .aspectRatio(media.aspectRatio)
        .let { mod ->
            if (clickable) {
                mod.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { _ ->
                            if (!isActive) {
                                start()
                            } else {
                                showControls = !showControls
                            }
                        },
                        onDoubleTap = goFullscreen?.let { { _ -> goFullscreen() } })
                }
            } else {
                mod
            }
        }
    Box(
        modifier = modifier
    ) {

        VideoPlayer(
            media,
            key,
            placeholder = placeholder,
            startPlaying = autostart,
            mute = startMuted
        )
        if (isActive && !presentationState.coverSurface) {
            Crossfade(
                targetState = showControls,
            ) { state ->
                Box(modifier = Modifier.fillMaxSize()) {
                    if (state) {
                        FullPlayerControls(goFullscreen = goFullscreen)
                    } else {
                        AlwaysOnInfo(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(all = 8.dp)
                        )
                    }
                }
            }
        } else {
            LoadDecoration(showDecoration, loading, cartouche)
        }
    }
}

@Composable
private fun BoxScope.LoadDecoration(
    showDecoration: Boolean,
    loading: Boolean,
    cartouche: @Composable (() -> Unit)?,
) {
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


@Composable
private fun BoxScope.FullPlayerControls(
    buttonSize: Dp = 64.dp,
    buttonPlacement: Alignment = Alignment.Center,
    barPlacement: Alignment = Alignment.BottomCenter,
    goFullscreen: (() -> Unit)?,
) {
    PlayerButtons(
        modifier = Modifier.align(buttonPlacement),
        buttonSize = buttonSize,
        goFullscreen = goFullscreen,
        fullscreenButtonIcon = Icons.Default.Fullscreen,
        fullscreenButtonContentDescription = stringResource(R.string.video_open_fullscreen)
    )
    PlayerControls(
        fullscreenButton = null,
        modifier = Modifier
            .fillMaxWidth()
            .align(barPlacement)
    )
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerButtons(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 64.dp,
    goFullscreen: (() -> Unit)? = null,
    fullscreenButtonIcon: ImageVector? = null,
    fullscreenButtonContentDescription: String? = null,
) {
    val player = remember { VideoPlayerManager.getInstance() }
    val theme = LocalTheme.current
    val showMuteButton by VideoPlayerManager.hasAudio.collectAsState()
    val frac = if (showMuteButton && goFullscreen != null) 0.66f else 0.5f
    val textColor = contentColorFor(theme.highlight)
    val buttonModifier = Modifier
        .clip(CircleShape)
        .background(theme.highlight)
        .size(buttonSize)
    Row(
        modifier = modifier.fillMaxSize(frac),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showMuteButton) {
            MuteButton(
                player,
                tint = textColor,
                colors = IconButtonDefaults.iconButtonColors()
                    .copy(contentColor = Color.White),
                modifier = buttonModifier
            )
        }
        PlayPauseButton(
            player,
            tint = textColor,
            modifier = buttonModifier
        )
        IconButton(onClick = { goFullscreen?.invoke() }, modifier = buttonModifier) {
            Icon(
                fullscreenButtonIcon!!,
                tint = textColor,
                contentDescription = fullscreenButtonContentDescription
            )
        }
    }
}
