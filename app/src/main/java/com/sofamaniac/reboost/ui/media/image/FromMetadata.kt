package com.sofamaniac.reboost.ui.media.image

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.sofamaniac.reboost.domain.model.MediaResource
import com.sofamaniac.reboost.domain.model.PostData
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.lang.Float
import kotlin.Boolean
import kotlin.Unit

@Composable
fun FromPreview(
    post: PostData,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true
) {
    val images = post.preview!!
    val image = images.images[0].source
    val url = image.url
    val x = image.width
    val y = Float.max(image.height.toFloat(), 1.0.toFloat())
    post.thumbnail.uri.toHttpUrlOrNull()?.toUrl()
    val modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(x / y)
    TransformableImage(
        url,
        contentDescription = post.title,
        modifier = modifier,
        enabled = allowTransformation
    )
}

@Composable
fun ImageView(
    media: MediaResource,
    modifier: Modifier = Modifier,
    allowTransformation: Boolean = true,
    onTap: (() -> Unit)? = null
) {
    val url = media.url
    val ratio = media.aspectRatio
    val modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratio)
    TransformableImage(
        url,
        contentDescription = "Image",
        contentScale = ContentScale.Fit,
        modifier = modifier,
        enabled = allowTransformation,
        onTap = { onTap?.invoke() }
    )
}

