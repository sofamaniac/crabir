package com.sofamaniac.crabir.ui.media.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.sofamaniac.crabir.data.remote.dto.post.Preview
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality

@Composable
fun ImageView(
    preview: Preview,
    contentDescription: String,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    blur: Boolean = false,
    contentScale: ContentScale = ContentScale.Fit,
    quality: Quality,
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
        image.url,
        modifier = modifier,
        contentDescription = contentDescription,
        placeholderAspectRatio = image.aspectRatio,
        contentScale = contentScale,
        onClick = onClick,
        onZoomChange = onZoomChange,
        allowZoom = allowTransformation,
        blur = blur
    )
}

@Composable
fun ImageView(
    media: MediaResource,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    onZoomChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
) {
    TransformableImage(
        media.url,
        onZoomChange = onZoomChange,
        contentDescription = "Image",
        contentScale = ContentScale.Fit,
        modifier = modifier,
        allowZoom = allowTransformation,
        onClick = onClick,
    )
}

@Composable
fun ImageView(
    post: PostData,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    blur: Boolean = false,
    contentScale: ContentScale = ContentScale.Fit,
    quality: Quality,
    onZoomChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
) {
    if (post.preview != null) {
        ImageView(
            post.preview,
            contentDescription = post.title,
            onZoomChange = onZoomChange,
            modifier = modifier
                .fillMaxWidth(),
            allowTransformation = allowTransformation,
            blur = blur,
            contentScale = contentScale,
            quality = quality,
            onClick = onClick
        )
    } else {
        TransformableImage(
            source = post.url,
            contentDescription = post.title,
            modifier = modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit,
            onClick = onClick,
        )
    }
}