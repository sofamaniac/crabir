package com.sofamaniac.crabir.ui.media.videoPlayer

import android.content.Context
import android.util.Log
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
    private var player: ExoPlayer? = null

    private var _currentUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    var currentUrl: StateFlow<String?> = _currentUrl.asStateFlow()
    private var _hasFirstFrame = MutableStateFlow(false)
    val hasFirstFrame = _hasFirstFrame.asStateFlow()

    private var _hasAudio = MutableStateFlow(false)
    val hasAudio = _hasAudio.asStateFlow()

    fun getInstance(context: Context): ExoPlayer {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                repeatMode = REPEAT_MODE_ONE
                addListener(object : androidx.media3.common.Player.Listener {
                    override fun onRenderedFirstFrame() {
                        super.onRenderedFirstFrame()
                        _hasFirstFrame.update { true }
                    }

                    override fun onTracksChanged(tracks: Tracks) {
                        super.onTracksChanged(tracks)
                        _hasAudio.update {
                            tracks.groups.any { it.type == C.TRACK_TYPE_AUDIO }
                        }
                    }
                })
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
            _hasFirstFrame.update { false }
            prepare()
        }
        _currentUrl.update { uri }
    }

    fun releasePlayer() {
        stopPlayer()
        player?.release()
        player = null
    }

    fun stopPlayer() {
        Log.d("VideoPlayerManager", "Stopping player")
        player?.stop()
        _currentUrl.update { null }
        _hasFirstFrame.update { false }
    }

    fun setTrack(group: Tracks.Group, index: Int = 0) {
        player?.trackSelectionParameters = player?.trackSelectionParameters
            ?.buildUpon()
            ?.setOverrideForType(TrackSelectionOverride(group.mediaTrackGroup, index))
            ?.build() ?: return
    }

    fun setAutoTrack() {
        player?.trackSelectionParameters = player?.trackSelectionParameters
            ?.buildUpon()
            ?.clearOverrides()
            ?.build() ?: return
    }

    fun isAuto(): Boolean {
        return player?.trackSelectionParameters?.overrides?.isEmpty() ?: true
    }


}