package com.sofamaniac.crabir.ui.post

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
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.FullscreenGalleryRoute
import com.sofamaniac.crabir.navigation.FullscreenImageRoute
import com.sofamaniac.crabir.navigation.FullscreenVideoRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.media.image.TransformableImage
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun Thumbnail(
    post: PostData,
    blur: Boolean = false,
) {
    val thumbnailURL = post.getThumbnailUrl()
    val uriHandler = LocalUriHandler.current
    //val fullscreenManager = LocalFullscreenHandler.current!!
    val navController = LocalNavController.current
    val goFullscreen = {
        when (post.kind) {
            Kind.Image -> navController?.navigate(FullscreenImageRoute(post.name))
            Kind.Gallery -> navController?.navigate(FullscreenGalleryRoute(post.name))
            Kind.Video -> navController?.navigate(FullscreenVideoRoute(post.name))
            else -> uriHandler.openUri(post.url)
        }
        Unit
    }
    val modifier = Modifier
        .fillMaxWidth(fraction = 0.2f)
        .aspectRatio(1f)
        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
        .clickable(onClick = goFullscreen)
    if (thumbnailURL != null) {
        TransformableImage(
            source = thumbnailURL,
            contentDescription = post.title,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            blur = blur,
            allowZoom = false,
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
