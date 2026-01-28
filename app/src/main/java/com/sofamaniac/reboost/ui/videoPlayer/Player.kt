package com.sofamaniac.reboost.ui.videoPlayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.buttons.SeekBackButton
import androidx.media3.ui.compose.material3.buttons.SeekForwardButton
import androidx.media3.ui.compose.material3.indicator.PositionAndDurationText
import com.sofamaniac.reboost.data.remote.dto.post.PostImageSource

class VideoPlayerManager {
    var currentPlayer: ExoPlayer? = null
        private set

    fun setPlayer(player: ExoPlayer) {
        if (currentPlayer != player) {
            currentPlayer?.pause()
        }
        currentPlayer = player
    }

    fun tryRelease(player: ExoPlayer) {
        if (currentPlayer == player) {
            currentPlayer = null
        }
    }
}

val LocalVideoPlayerManager = compositionLocalOf { VideoPlayerManager() }


@Composable
fun VideoPlayer(source: PostImageSource) {
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

    // Manage lifecycle events
    DisposableEffect(source.url) {
        onDispose {
            videoPlayerManager.tryRelease(player)
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
                    player.play()
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

        if (showControls) {
            Row() {
                SeekBackButton(player = player!!)
                PlayPauseButton(player = player!!)
                SeekForwardButton(player = player!!)
                PositionAndDurationText(player = player!!)
            }
        }
    }
}