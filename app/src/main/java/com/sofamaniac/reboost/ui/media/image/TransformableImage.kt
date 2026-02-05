package com.sofamaniac.reboost.ui.media.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun TransformableImage(
    url: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    onTap: ((Offset) -> Unit)? = null
) {
    val zoomState = rememberZoomState()
    val modifier = if (enabled) {
        modifier.zoomable(
            zoomState,
            onTap = onTap,
            scrollGesturePropagation = ScrollGesturePropagation.NotZoomed
        )

    } else {
        modifier
    }
    AsyncImage(
        url, contentDescription, modifier, contentScale = contentScale,
        onSuccess = { state ->
            zoomState.setContentSize(state.painter.intrinsicSize)
        })
}