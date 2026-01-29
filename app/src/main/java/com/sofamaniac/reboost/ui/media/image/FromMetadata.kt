package com.sofamaniac.reboost.ui.media.image

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.PostData
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.lang.Float
import kotlin.toString

@Composable
fun FromMetadata(post: PostData, modifier: Modifier = Modifier) {
    val images = post.getPreview()!!
    val image = images.images[0].source
    val url = image.url
    val x = image.width
    val y = Float.max(image.height.toFloat(), 1.0.toFloat())
    post.thumbnail.uri.toHttpUrlOrNull()?.toUrl()
    AsyncImage(
        model = url.toString(),
        contentDescription = post.title,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .aspectRatio(x.toFloat() / y),
        contentScale = ContentScale.Companion.Fit,
    )
}

@Composable
fun ImageView(metadata: MediaMetadata.Image, modifier: Modifier = Modifier) {
    val url = metadata.s!!.url.toString()
    val ratio = metadata.s.width.toFloat() / metadata.s.height.toFloat()
    AsyncImage(
        model = url,
        contentDescription = "Image",
        modifier = Modifier.Companion
            .fillMaxWidth()
            .aspectRatio(ratio),
        contentScale = ContentScale.Companion.Fit,
    )
}

@Composable
fun ImageView(metadata: MediaMetadata.Gif, modifier: Modifier = Modifier) {
    val url = metadata.s!!.gifUrl.toString()
    val ratio = metadata.s.width.toFloat() / metadata.s.height.toFloat()
    // TODO choose the proper one based on resolution
    metadata.preview[0].url.toString()
    LocalContext.current
    AsyncImage(
        model = url,
        contentDescription = "Gif",
        modifier = Modifier.Companion
            .fillMaxWidth()
            .aspectRatio(ratio),
        contentScale = ContentScale.Companion.Fit,
    )
}