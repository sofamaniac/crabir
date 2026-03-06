package com.sofamaniac.reboost.ui.media.image

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.domain.model.MediaResource
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun TransformableImage(
    source: MediaResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    onClick: () -> Unit = {}
) {
//    ZoomableView(modifier, onClick = onClick) {
//        AsyncImage(
//            url, contentDescription, modifier, contentScale = contentScale,
//            onSuccess = { state ->
//                //zoomState.setContentSize(state.painter.intrinsicSize)
//            }
//        )
//    }
    val zoomState = rememberZoomState()
    val modifier = if (enabled) {
        modifier.zoomable(
            zoomState = zoomState,
            scrollGesturePropagation = ScrollGesturePropagation.NotZoomed,
            onTap = { onClick() }
        )
    } else {
        modifier
    }
    AsyncImage(
        source.url,
        contentDescription,
        modifier.aspectRatio(source.aspectRatio),
        contentScale = contentScale,
        onSuccess = { state ->
            zoomState.setContentSize(state.painter.intrinsicSize)
        })

}