package com.sofamaniac.crabir.ui.media.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.domain.model.MediaResource
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage

@Composable
fun TransformableImage(
    source: MediaResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    onClick: () -> Unit = {}
) {
    if (enabled) {
        ZoomableAsyncImage(
            source.url,
            contentDescription,
            modifier,
            contentScale = contentScale,
            onClick = { onClick() },
        )
    } else {
        AsyncImage(
            source.url,
            contentDescription,
            modifier,
            contentScale = contentScale,
        )
    }

}