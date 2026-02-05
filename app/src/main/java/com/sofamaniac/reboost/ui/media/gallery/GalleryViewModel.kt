package com.sofamaniac.reboost.ui.media.gallery

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.Gallery
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel(assistedFactory = GalleryViewModel.Factory::class)
class GalleryViewModel @AssistedInject constructor(
    @Assisted val gallery: Gallery,
    @Assisted val context: Context
) : ViewModel() {

    var players: Map<String, ExoPlayer> = emptyMap()
        private set
    var videos: List<String> = emptyList()
    private var currentMediaItem: String? = null

    init {
        for (image in gallery.images) {
            val metadata = gallery.mediaMetadata[image.mediaId]!!
            if (metadata is MediaMetadata.Gif) {
                val mediaItem = MediaItem.fromUri(metadata.s!!.mp4Url!!)
                val player = ExoPlayer.Builder(context).build().apply {
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    addMediaItem(mediaItem)
                    prepare()
                }
                players += image.mediaId to player
                videos += image.mediaId
            }
        }
    }

    fun clearAll() {
        for (player in players) {
            player.value.release()
        }
        players = emptyMap()
    }

    @AssistedFactory
    interface Factory {
        fun create(gallery: Gallery, context: Context): GalleryViewModel
    }

}