package com.sofamaniac.reboost.ui.media.videoPlayer

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object VideoPlayerManager {
    private var player: ExoPlayer? = null

    private var _currentUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    var currentUrl: StateFlow<String?> = _currentUrl.asStateFlow()


    fun getInstance(context: Context): ExoPlayer {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                repeatMode = REPEAT_MODE_ONE
            }
        }
        return player!!
    }

    fun setMediaItem(uri: String) {
        if (_currentUrl.value == uri) {
            return
        }
        val mediaItem = MediaItem.fromUri(uri)
        player?.apply {
            setMediaItem(mediaItem)
            prepare()
        }
        _currentUrl.value = uri
        Log.d("VideoPlayerManager", "Setting media item $uri")
    }

    fun playPlayer() {
        player?.play()

    }

    fun resumePlayer() {
        player?.play()
    }

    fun pausePlayer() {
        player?.pause()
    }

    fun releasePlayer() {
        player?.release()
        player = null
    }

    fun stopPlayer() {
        Log.d("VideoPlayerManager", "Stopping player")
        player?.stop()
        _currentUrl.value = null
    }


}