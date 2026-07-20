package com.sofamaniac.crabir.ui.media.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import com.sofamaniac.crabir.domain.model.MediaResource
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState

@Composable
fun TransformableImage(
    source: MediaResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    zoomableState: ZoomableState,
    onClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.25).build()
    }.diskCache {
        // 2go
        DiskCache.Builder().maxSizeBytes(2 * 1024 * 1024).build()
    }.build()
    if (enabled) {
        ZoomableAsyncImage(
            source.url,
            contentDescription,
            modifier,
            imageLoader = imageLoader,
            state = rememberZoomableImageState(zoomableState),
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