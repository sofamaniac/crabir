package com.sofamaniac.reboost.ui.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.data.remote.dto.post.PostImageSource
import com.sofamaniac.reboost.domain.model.Gallery
import com.sofamaniac.reboost.ui.media.image.ImageView
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayer

@Composable
fun Gallery(
    gallery: Gallery, modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size }),
    onTap: (() -> Unit)? = null,
    decorations: @Composable (BoxScope.() -> Unit)? = null,
) {
    val minRatio = gallery.mediaMetadata.map { metadata ->
        when (val data = metadata.value) {
            is MediaMetadata.Image -> {
                data.s!!.ratio
            }

            is MediaMetadata.Gif -> {
                data.s!!.ratio
            }

            else -> {
                1f
            }
        }
    }.min()
    var modifier = modifier
        .fillMaxWidth()
        .aspectRatio(minRatio)

    modifier = if (onTap != null) {
        modifier.clickable(onClick = onTap)
    } else {
        modifier
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(state = state, modifier = Modifier.fillMaxSize()) { page ->
            val mediaId = gallery.images[page].mediaId
            val metadata: MediaMetadata? = gallery.mediaMetadata[mediaId]
            if (metadata != null) {
                when (metadata) {
                    is MediaMetadata.Image -> ImageView(metadata)
                    is MediaMetadata.Gif -> VideoPlayer(
                        PostImageSource(
                            metadata.s?.mp4Url,
                            metadata.s?.width!!,
                            metadata.s.height
                        )
                    )

                    else -> {
                        Text("No luck my friend (${metadata.javaClass.simpleName})")
                    }
                }
            }
        }
        decorations?.invoke(this)
    }
}