package com.sofamaniac.crabir.ui.media.videoPlayer

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object VideoPlayerManager {
    private lateinit var player: ExoPlayer

    private var _currentUrl: MutableStateFlow<Uri?> = MutableStateFlow(null)

    private var _currentKey: MutableStateFlow<String?> = MutableStateFlow(null)
    var currentKey: StateFlow<String?> = _currentKey.asStateFlow()

    private var _hasAudio = MutableStateFlow(false)
    val hasAudio = _hasAudio.asStateFlow()

    fun initialize(context: Context) {
        player = ExoPlayer.Builder(context).build().apply {
            repeatMode = REPEAT_MODE_ONE
            addListener(object : androidx.media3.common.Player.Listener {

                override fun onTracksChanged(tracks: Tracks) {
                    super.onTracksChanged(tracks)
                    _hasAudio.update {
                        tracks.groups.any { it.type == C.TRACK_TYPE_AUDIO }
                    }
                }
            })
        }
    }

    fun getInstance(): ExoPlayer {
        return player
    }

    fun setMediaItem(uri: String, key: String, playWhenReady: Boolean = true, volume: Float = 0f) {
        val newUri = uri.toUri()
        _currentKey.value = key
        if (_currentUrl.value.checkEquality(newUri)) {
            player.apply {
                this.playWhenReady = playWhenReady
                this.volume = volume
            }
            return
        }
        val mediaItem = MediaItem.fromUri(uri)
        player.apply {
            Log.d("VideoPlayerManager", "Setting media item $uri")
            setMediaItem(mediaItem)
            this.playWhenReady = playWhenReady
            this.volume = volume
            prepare()
        }
        _currentUrl.update { newUri }
    }

    fun releasePlayer() {
        stopPlayer()
        player.release()
    }

    fun stopPlayer() {
        Log.d("VideoPlayerManager", "Stopping player")
        player.stop()
        _currentUrl.update { null }
        _currentKey.update { null }
    }

    fun setTrack(group: Tracks.Group, index: Int = 0) {
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setOverrideForType(TrackSelectionOverride(group.mediaTrackGroup, index))
            .build() ?: return
    }

    fun setAutoTrack() {
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .clearOverrides()
            .build() ?: return
    }

    fun isAuto(): Boolean {
        return player.trackSelectionParameters.overrides?.isEmpty() ?: true
    }


}

internal fun Uri?.checkEquality(other: Uri?): Boolean {
    return this?.host == other?.host && this?.path == other?.path
}
