package com.sofamaniac.reboost.ui.media.image

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.data.remote.dto.post.Preview
import com.sofamaniac.reboost.domain.model.MediaResource
import com.sofamaniac.reboost.domain.model.PostData

@Composable
fun FromPreview(
    preview: Preview,
    contentDescription: String,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    onClick: () -> Unit = {}
) {

    val image = preview.images[0].source.toMediaResource()
    val modifier = modifier
        .fillMaxWidth()
        .aspectRatio(image.aspectRatio)
    TransformableImage(
        image,
        contentDescription = contentDescription,
        modifier = modifier,
        enabled = allowTransformation,
        onClick = onClick
    )
}

@Composable
fun ImageView(
    media: MediaResource,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    onClick: () -> Unit = {}
) {
    val modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(media.aspectRatio)
    TransformableImage(
        media,
        contentDescription = "Image",
        contentScale = ContentScale.Fit,
        modifier = modifier,
        enabled = allowTransformation,
        onClick = onClick
    )
}

@Composable
fun ImageView(
    post: PostData,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    onClick: () -> Unit = {},
) {
    if (post.preview != null) {
        FromPreview(
            post.preview,
            post.title,
            modifier = modifier,
            allowTransformation = allowTransformation,
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