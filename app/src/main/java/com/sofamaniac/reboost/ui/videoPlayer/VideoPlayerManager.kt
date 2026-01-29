package com.sofamaniac.reboost.ui.videoPlayer

import androidx.compose.runtime.compositionLocalOf
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerManager {
    var currentPlayer: ExoPlayer? = null
        private set

    fun setPlayer(player: ExoPlayer) {
        if (currentPlayer != player) {
            currentPlayer?.stop()
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