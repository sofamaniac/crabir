package com.sofamaniac.crabir.ui.media.gallery

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.Gallery
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel


@KoinViewModel
class GalleryViewModel(
    @InjectedParam val gallery: Gallery,
    val context: Context
) : ViewModel() {

    var players: Map<String, ExoPlayer> = emptyMap()
        private set
    var videos: List<String> = emptyList()
    private var currentMediaItem: String? = null

    init {
        for (image in gallery.images) {
            val metadata = gallery.mediaMetadata[image.mediaId]!!
            if (metadata is MediaMetadata.Gif) {
                val mediaItem = MediaItem.fromUri(metadata.source!!.mp4Url!!)
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

}