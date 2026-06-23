package com.sofamaniac.crabir.ui.media.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.data.remote.dto.post.Preview
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState

@Composable
fun FromPreview(
    preview: Preview,
    contentDescription: String,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit,
    quality: Quality,
    zoomableState: ZoomableState,
    onClick: () -> Unit = {}
) {
    val preview = preview.images[0]
    val image = when (quality) {
        Quality.Source -> preview.source
        Quality.High -> preview.resolutions.last()
        Quality.Medium -> preview.resolutions[preview.resolutions.size / 2]
        Quality.Low -> preview.resolutions.first()
    }.toMediaResource()
    TransformableImage(
        image,
        contentDescription = contentDescription,
        modifier = modifier,
        enabled = allowTransformation,
        contentScale = contentScale,
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
    onClick: () -> Unit = {}
) {
    LaunchedEffect(zoomableState.contentTransformation.scale) {
        onZoomChange(zoomableState.contentTransformation.scaleMetadata.userZoom)
    }
    TransformableImage(
        media,
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
    onClick: () -> Unit = {},
) {
    if (post.preview != null) {
        FromPreview(
            post.preview,
            post.title,
            modifier = modifier,
            allowTransformation = allowTransformation,
            contentScale = contentScale,
            quality = quality,
            zoomableState = zoomableState,
            onClick = onClick
        )
    } else {
        AsyncImage(
            model = post.url,
            contentDescription = post.title,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Fit,
        )
    }
}