/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownloadOff
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality
import com.sofamaniac.crabir.navigation.FullscreenImageRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.views.ImageHeight
import com.sofamaniac.crabir.ui.SaveToHistory
import com.sofamaniac.crabir.ui.media.FullscreenBottomBar
import com.sofamaniac.crabir.ui.media.FullscreenTopBar
import com.sofamaniac.crabir.ui.media.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.image.DownloadButton
import com.sofamaniac.crabir.ui.media.image.ImageView
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

internal fun PostData.aspectRatio(): Float {
    return preview?.images[0]?.source?.toMediaResource()?.aspectRatio ?: 1f
}

@Composable
fun PostImage(
    post: PostData,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    blur: Boolean = false,
    goFullscreen: (Route) -> Unit,
) {
    val dataSettings = LocalDataSettings.current
    val quality = if (LocalContext.current.onWifiConnection) {
        dataSettings.imageQuality.onWifi
    } else {
        dataSettings.imageQuality.onMobile
    }
    val loadImage = when (dataSettings.imageQuality.loadImage) {
        com.sofamaniac.crabir.settings.data.NetworkPolicy.Always -> true
        com.sofamaniac.crabir.settings.data.NetworkPolicy.Never -> false
        com.sofamaniac.crabir.settings.data.NetworkPolicy.OnWifi -> LocalContext.current.onWifiConnection
    }
    val goFullscreen = {
        goFullscreen(FullscreenImageRoute(post.name))
    }
    val viewSettings = LocalViewSettings.current
    val blurredImage = post.getObfuscated()
    val imageModifier = modifier.let {
        when (viewSettings.cardSettings.imageHeight) {
            ImageHeight.Full -> modifier.aspectRatio(post.aspectRatio())
            ImageHeight.Fixed -> modifier.height(200.dp)
            ImageHeight.Screen -> modifier
                .aspectRatio(post.aspectRatio())
                .heightIn(max = LocalWindowInfo.current.containerDpSize.height.times(0.8f))
        }
    }
    Box(modifier = modifier.clickable(enabled = enabled, onClick = goFullscreen)) {
        if (!loadImage) {
            UnloadedPlaceHolder(
                modifier
                    .height(150.dp)
                    .clickable(enabled = enabled, onClick = goFullscreen)
            )
        } else if (blurredImage == null || !blur) {
            ImageView(
                post,
                quality = quality,
                modifier = imageModifier,
                allowTransformation = false,
                blur = blur
            )
        } else {
            ImageView(
                media = blurredImage,
                modifier = imageModifier,
                allowTransformation = false,
            )
        }
    }
}

@Composable
fun UnloadedPlaceHolder(modifier: Modifier = Modifier) {
    Box(modifier.background(Color.Gray)) {
        Icon(
            Icons.Default.FileDownloadOff,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun FullscreenImageView(
    post: Fullname,
    viewModel: PostDataViewModel = koinViewModel { parametersOf(post) },
    dismiss: () -> Unit,
) {
    val dataSettings = LocalDataSettings.current
    val initialQuality = if (LocalContext.current.onWifiConnection) {
        dataSettings.imageQuality.onWifi
    } else {
        dataSettings.imageQuality.onMobile
    }
    var quality by remember { mutableStateOf(initialQuality) }
    var showDecorations by remember { mutableStateOf(true) }
    val postData by viewModel.post.collectAsState(initial = null)
    var enableDismiss by remember { mutableStateOf(true) }
    SaveToHistory(post)
    VerticalSwipeToDismiss(
        onDismiss = dismiss,
        enabled = true,
        topBar = {
            if (postData == null) return@VerticalSwipeToDismiss
            FullscreenTopBar(showDecorations, actions = {
                DownloadButton(postData!!.getSourceUrl().toUri())
                if (quality != Quality.Source) {
                    IconButton(onClick = { quality = Quality.Source }) {
                        Icon(
                            Icons.Default.Hd,
                            contentDescription = "Load highest quality available",
                            tint = Color.White
                        )
                    }
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
                allowTransformation = true, // zoomableState = zoomableState,
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
