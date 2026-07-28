package com.sofamaniac.crabir.ui.media.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.sofamaniac.crabir.data.remote.dto.post.Preview
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState

@Composable
fun ImageView(
    preview: Preview,
    contentDescription: String,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    quality: Quality,
    zoomableState: ZoomableState,
    onZoomChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val preview = preview.images[0]
    val resolutions = preview.resolutions.sortedBy { it.width }
    val image = when (quality) {
        Quality.Source -> preview.source
        Quality.High -> resolutions.last()
        Quality.Medium -> resolutions[preview.resolutions.size / 2]
        Quality.Low -> resolutions.first()
    }.toMediaResource()
    TransformableImage(
        image,
        contentDescription = contentDescription,
        contentScale = contentScale,
        onZoomChange = onZoomChange,
        modifier = modifier,
        enabled = allowTransformation,
        zoomableState = zoomableState,
        onClick = onClick
    )
}

@Composable
fun ImageView(
    media: MediaResource,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    zoomableState: ZoomableState = rememberZoomableState(),
    onZoomChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
) {
    TransformableImage(
        media,
        onZoomChange = onZoomChange,
        contentDescription = "Image",
        contentScale = ContentScale.Fit,
        modifier = modifier,
        enabled = allowTransformation,
        onClick = onClick,
        zoomableState = zoomableState,
    )
}

@Composable
fun ImageView(
    post: PostData,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    quality: Quality,
    zoomableState: ZoomableState = rememberZoomableState(),
    onZoomChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
) {
    if (post.preview != null) {
        ImageView(
            post.preview,
            post.title,
            onZoomChange = onZoomChange,
            modifier = modifier
                .fillMaxWidth(),
            allowTransformation = allowTransformation,
            contentScale = contentScale,
            quality = quality,
            zoomableState = zoomableState,
            onClick = onClick
        )
    } else {
        TransformableImage(
            source = post.url,
            contentDescription = post.title,
            modifier = modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit,
            zoomableState = zoomableState,
            onClick = onClick,
        )
    }
}