/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalSharedTransitionScope
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Quality
import com.sofamaniac.crabir.navigation.FullscreenImageRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.filters.rememberFiltersSettings
import com.sofamaniac.crabir.ui.SharedElementKey
import com.sofamaniac.crabir.ui.SharedElementType
import com.sofamaniac.crabir.ui.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.image.DownloadButton
import com.sofamaniac.crabir.ui.media.image.ImageView

private fun PostData.getImage(): MediaResource {
    return if (preview != null) {
        preview.images[0].source.toMediaResource()
    } else {
        MediaResource(url, -1f, -1, -1)
    }
}

@Composable
fun PostImage(
    post: PostData,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    goFullscreen: (Route) -> Unit
) {
    val quality = Quality.High
    val goFullscreen = {
//        fullscreenManager.push {
//            FullscreenImageView(post)
//        }
        goFullscreen(FullscreenImageRoute(post.name))
    }
    val filters = rememberFiltersSettings()
    val blur = post.spoiler || (post.over18 && filters.blurNSFW)
    val mediaResource = post.getImage()
    val modifier = if (blur) {
        modifier.blur(40.dp)
    } else {
        modifier
    }.let { modifier ->
        if (mediaResource.aspectRatio > 0) {
            modifier.aspectRatio(mediaResource.aspectRatio)
        } else {
            modifier
        }
    }
    ImageView(
        post,
        quality = quality,
        modifier = modifier.clickable(enabled = enabled) {
            goFullscreen()
        },
        allowTransformation = false
    )

}

@Composable
fun FullscreenImageView(
    post: Fullname,
    viewModel: PostDataViewModel = hiltViewModel<PostDataViewModel, PostDataViewModel.Factory> { factory ->
        factory.create(post.name)
    },
    dismiss: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var quality by remember { mutableStateOf(Quality.High) }
    var showDecorations by remember { mutableStateOf(true) }
    val postData by viewModel.post.collectAsState(initial = null)
    if (postData == null) return
    val sharedTransitionScope = LocalSharedTransitionScope.current
    with(sharedTransitionScope) {
        VerticalSwipeToDismiss(
            onDismiss = dismiss,
            topBar = {
                FullscreenTopBar(showDecorations, actions = {
                    if (quality != Quality.Source) DownloadButton(postData!!.getSourceUrl().toUri())
                    IconButton(onClick = { quality = Quality.Source }) {
                        Icon(Icons.Default.Hd, contentDescription = null)
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
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(
                            key = SharedElementKey(
                                postData!!.name,
                                SharedElementType.Content
                            )
                        ),
                        animatedVisibilityScope
                    ),
                quality = quality,
                onClick = {
                    showDecorations = !showDecorations
                }
            )
        }
    }
}

internal fun PostData.getSourceUrl(): String = preview?.images?.firstOrNull()?.source?.url ?: url