package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.LocalFullscreenHandler
import com.sofamaniac.reboost.domain.model.Kind
import com.sofamaniac.reboost.domain.model.PostData
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun Thumbnail(post: PostData) {
    val thumbnailURL = post.getThumbnailUrl()
    val uriHandler = LocalUriHandler.current
    val fullscreenManager = LocalFullscreenHandler.current!!
    val goFullscreen = {
        when (post.kind) {
            Kind.Image -> fullscreenManager.push { FullscreenImageView(post) }
            Kind.Gallery -> fullscreenManager.push {
                FullscreenGallery(
                    post,
                    gallery = post.gallery!!
                )
            }
            // TODO: thumbnail video post
            //Kind.Video -> fullscreenManager.push { FullscreenVideo(post) }

            else -> uriHandler.openUri(post.url)
        }
    }
    val modifier = Modifier
        .fillMaxWidth(fraction = 0.2f)
        .aspectRatio(1f)
        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
        .clickable(onClick = goFullscreen)
    if (thumbnailURL != null) {
        AsyncImage(
            model = thumbnailURL,
            contentDescription = post.title,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } else {
        Icon(
            Icons.Default.Link,
            contentDescription = "Link",
            modifier = modifier
                .background(Color.Gray)
                .rotate(45f)
                .scale(0.5f)
        )
    }
}

private fun PostData.getThumbnailUrl(): String? {
    val thumbnailUrl = thumbnail.uri.toHttpUrlOrNull()
    val previewUrl =
        preview?.images?.firstOrNull()?.resolutions?.firstOrNull()?.url?.toHttpUrlOrNull()
    return thumbnailUrl?.toString() ?: previewUrl?.toString()
}
