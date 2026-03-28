package com.sofamaniac.crabir.ui.media.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.data.remote.dto.post.Preview
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData

@Composable
fun FromPreview(
    preview: Preview,
    contentDescription: String,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    onClick: () -> Unit = {}
) {

    val image = preview.images[0].source.toMediaResource()
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