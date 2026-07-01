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
import me.saket.telephoto.zoomable.rememberZoomableState
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
    val variant = image.variants?.obfuscated// ?: image.variants?.nsfw
    return variant?.resolutions?.minByOrNull { it.width }?.toMediaResource()
}

@Composable
fun PostImage(
    post: PostData,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    blur: Boolean = false,
    goFullscreen: (Route) -> Unit
) {
    val quality = Quality.High
    val goFullscreen = {
        goFullscreen(FullscreenImageRoute(post.name))
    }
    val mediaResource = post.getImage()
    val blurStyle = crabirBlurStyle()
    val blurred = post.getObfuscated()
    val modifier = Modifier
        .hazeEffect {
            inputScale = HazeInputScale.Fixed(0.5f)
            blurEffect {
                style = blurStyle
                // Blur only if not obfuscated preview is available
                blurEnabled = blur && blurred == null
            }
        }.then(modifier)
        .let { modifier ->
            if (mediaResource.aspectRatio > 0) {
                modifier.aspectRatio(mediaResource.aspectRatio)
            } else {
                modifier
            }
        }
    Box(modifier.clickable(enabled = enabled) {
        goFullscreen()
    }) {
        ImageView(
            post,
            quality = quality,
            modifier = Modifier.fillMaxSize(),
            allowTransformation = false
        )
        if (blurred != null && blur) {
            AsyncImage(
                blurred.url,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
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
    if (postData == null) return
    val zoomableState = rememberZoomableState()
        VerticalSwipeToDismiss(
            onDismiss = dismiss,
            enabled = zoomableState.contentTransformation.scaleMetadata.userZoom == 1.0f,
            topBar = {
                FullscreenTopBar(showDecorations, actions = {
                    if (quality != Quality.Source) DownloadButton(postData!!.getSourceUrl().toUri())
                    IconButton(onClick = { quality = Quality.Source }) {
                        Icon(Icons.Default.Hd, contentDescription = null, tint = Color.White)
                    }
                })
            },
            bottomBar = { FullscreenBottomBar(postData!!, showDecorations) },
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    showDecorations = !showDecorations
                },
        ) {
            ImageView(
                postData!!,
                zoomableState = zoomableState,
                modifier = Modifier
                    .fillMaxSize(),
                quality = quality,
                onClick = {
                    showDecorations = !showDecorations
                }
            )
        }
}

internal fun PostData.getSourceUrl(): String = preview?.images?.firstOrNull()?.source?.url ?: url