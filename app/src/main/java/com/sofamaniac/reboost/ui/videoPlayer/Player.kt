package com.sofamaniac.reboost.ui.videoPlayer

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util.getStringForTime
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.indicators.TimeText
import androidx.media3.ui.compose.material3.buttons.MuteButton
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.indicator.PositionAndDurationText
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.sofamaniac.reboost.data.remote.dto.post.PostImageSource
import com.sofamaniac.reboost.ui.Cartouche
import kotlinx.coroutines.delay


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    source: PostImageSource,
    cartouche: @Composable (() -> Unit)? = null,
) {
    var showControls by remember { mutableStateOf(false) }
    val videoPlayerManager = LocalVideoPlayerManager.current

    val context = LocalContext.current
    val mediaItem = MediaItem.fromUri(source.url!!)
    val width = source.width
    val height = source.height
    val player = remember(source.url) {
        ExoPlayer.Builder(context).build().apply {
            // Set MediaSource to ExoPlayer
            setMediaItem(mediaItem)
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            if (videoPlayerManager.currentPlayer == null) {
                videoPlayerManager.setPlayer(this@apply)
            }
            prepare()
        }
    }
    player.addAnalyticsListener(EventLogger())

    val playing = rememberPlayPauseButtonState(player).showPlay

    val lifecycleOwner = LocalLifecycleOwner.current

    // Manage lifecycle events
    DisposableEffect(source.url) {
        // Pause video when app in background
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    player.pause()
                }
//                Lifecycle.Event.ON_RESUME -> {
//                    player.play()
//                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            videoPlayerManager.tryRelease(player)
            player.clearVideoSurface()
            player.stop()
            player.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(width.toFloat() / height.toFloat())
            .onGloballyPositioned { coordinates ->
                val rect = coordinates.boundsInWindow()
                if (rect.height < height / 4) {
                    player.pause()
                    return@onGloballyPositioned
                }

                val screenWidth = context.resources.displayMetrics.widthPixels
                val screenHeight = context.resources.displayMetrics.heightPixels
                val screenCenterY = screenHeight / 2
                val screenCenterX = screenWidth / 2

                // Calculate video center
                val videoCenterY = (rect.top + rect.bottom) / 2
                val videoCenterX = (rect.left + rect.right) / 2

                // Distance from screen center
                val distanceFromCenterY = kotlin.math.abs(videoCenterY - screenCenterY)
                val distanceFromCenterX = kotlin.math.abs(videoCenterX - screenCenterX)

                // Check if this video's center is closest to screen center
                // This is simplified - ideally you'd compare with other videos
                val isCloseToCenter =
                    distanceFromCenterY < rect.height / 2 && distanceFromCenterX < rect.width / 2

                if (isCloseToCenter) {
                    videoPlayerManager.setPlayer(player)
                    //player.play()
                    player.playWhenReady = true
                }
            }
    ) {
        key(source.url) {
            ContentFrame(
                player = player,
                modifier = Modifier
                    .aspectRatio(width.toFloat() / height.toFloat())
                    .clickable { showControls = !showControls }
                    .fillMaxWidth()
            )
        }
        if (cartouche != null && !playing) {
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                cartouche
            }
        }

        if (showControls) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                PlayPauseButton(player = player)
                SeekProgressBar(player, Modifier.fillMaxWidth(0.75f))
                PositionAndDurationText(player = player)
            }
        } else {
            AlwaysOnInfo(player, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}


@Composable
fun AlwaysOnInfo(player: Player, modifier: Modifier = Modifier) {
    val backgroundColor = Color.Black.copy(alpha = 0.66f)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Cartouche(backgroundColor = backgroundColor) {
            MuteButton(player)
        }
        Cartouche(backgroundColor = backgroundColor) {
            RemainingTimeText(player, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun RemainingTimeText(player: Player, style: TextStyle? = null) {
    TimeText(player) {
        val remainingTime = this.durationMs - this.currentPositionMs
        val s = getStringForTime(remainingTime)
        Text(s, style = style ?: LocalTextStyle.current)
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekProgressBar(player: Player, modifier: Modifier = Modifier) {
    var sliderState by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(player) {
        while (true) {
            sliderState = player.currentPosition.toFloat() / player.duration.toFloat()
            delay(100)
        }
    }

    Slider(
        value = sliderState,
        onValueChange = {
            sliderState = it
            player.seekTo((it * player.duration).toLong())
        },
        modifier = modifier,
        thumb = {
            Box(
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            24.dp
                        )
                    )
                    .size(
                        24.dp
                    )
                    .background(color = MaterialTheme.colorScheme.primary),
            )
        },
        track = {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .height(
                        8.dp
                    )
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
            )
        },
    )
}