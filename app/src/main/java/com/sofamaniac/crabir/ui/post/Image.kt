/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality
import com.sofamaniac.crabir.navigation.FullscreenImageRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.ui.crabirBlurStyle
import com.sofamaniac.crabir.ui.media.FullscreenBottomBar
import com.sofamaniac.crabir.ui.media.FullscreenTopBar
import com.sofamaniac.crabir.ui.media.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.image.DownloadButton
import com.sofamaniac.crabir.ui.media.image.ImageView
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private fun PostData.getImage(): MediaResource {
    return if (preview != null) {
        preview.images[0].source.toMediaResource()
    } else {
        MediaResource(url, -1f, -1, -1)
    }
}

internal fun PostData.getObfuscated(): MediaResource? {
    val image = preview?.images[0] ?: return null
    val variant = image.variants?.obfuscated // ?: image.variants?.nsfw
    return variant?.resolutions?.minByOrNull { it.width }?.toMediaResource()
}

@Composable
fun PostImage(
    post: PostData,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    blur: Boolean = false,
    goFullscreen: (Route) -> Unit,
) {
    val quality = Quality.High
    val goFullscreen = {
        goFullscreen(FullscreenImageRoute(post.name))
    }
    val mediaResource = post.getImage()
    val blurStyle = crabirBlurStyle()
    val blurredImage = post.getObfuscated()
    val modifier = Modifier
        .fillMaxSize()
        .then(modifier)
        .let { modifier ->
            if (blur && blurredImage == null) {
                // Blur only if not obfuscated preview is available
                modifier.hazeEffect {
                    inputScale = HazeInputScale.Fixed(0.5f)
                    blurEffect {
                        style = blurStyle
                    }
                }
            } else {
                modifier
            }
        }
        .let { modifier ->
            if (mediaResource.hasValidAspectRatio) {
                modifier.aspectRatio(mediaResource.aspectRatio)
            } else {
                modifier
            }
        }
        .clickable(enabled = enabled, onClick = goFullscreen)
    if (blurredImage == null || !blur) {
        ImageView(
            post,
            quality = quality,
            modifier = modifier,
            allowTransformation = false
        )
    } else {
        AsyncImage(
            blurredImage.url,
            modifier = modifier,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }

}

@Composable
fun FullscreenImageView(
    post: Fullname,
    viewModel: PostDataViewModel = koinViewModel { parametersOf(post) },
    dismiss: () -> Unit,
) {
    var quality by remember { mutableStateOf(Quality.High) }
    var showDecorations by remember { mutableStateOf(true) }
    val postData by viewModel.post.collectAsState(initial = null)
    var enableDismiss by remember { mutableStateOf(true) }
    VerticalSwipeToDismiss(
        onDismiss = dismiss,
        enabled = true,
        topBar = {
            if (postData == null) return@VerticalSwipeToDismiss
            FullscreenTopBar(showDecorations, actions = {
                DownloadButton(postData!!.getSourceUrl().toUri())
                if (quality != Quality.Source) IconButton(onClick = { quality = Quality.Source }) {
                    Icon(Icons.Default.Hd, contentDescription = null, tint = Color.White)
                }
            })
        },
        bottomBar = {
            if (postData == null) return@VerticalSwipeToDismiss
            FullscreenBottomBar(postData!!, showDecorations)
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showDecorations = !showDecorations
            },
    ) {
        if (postData == null) return@VerticalSwipeToDismiss
        Box(modifier = Modifier.fillMaxSize()) {
            ImageView(
                postData!!,
                allowTransformation = true,
                //zoomableState = zoomableState,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                quality = quality,
                onZoomChange = {
                    enableDismiss = it == 1f || it == 0f
                },
                onClick = {
                    showDecorations = !showDecorations
                },
            )
        }
    }
}

internal fun PostData.getSourceUrl(): String = preview?.images?.firstOrNull()?.source?.url ?: url