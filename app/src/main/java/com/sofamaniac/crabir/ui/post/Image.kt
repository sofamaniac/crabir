/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.rememberFiltersSettings
import com.sofamaniac.crabir.ui.VerticalSwipeToDismiss
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
) {
    val fullscreenManager = LocalFullscreenHandler.current!!
    val goFullscreen = {
        fullscreenManager.push {
            FullscreenImageView(post)
        }
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
        modifier = modifier.clickable(enabled = enabled) {
            goFullscreen()
        },
        allowTransformation = false
    )

}

@Composable
fun FullscreenImageView(post: PostData) {
    var showDecorations by remember { mutableStateOf(true) }
    VerticalSwipeToDismiss(
        topBar = {
            FullscreenTopBar(showDecorations, actions = {})
        },
        bottomBar = { FullscreenBottomBar(post, showDecorations) },
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showDecorations = !showDecorations
            },
    ) {
        ImageView(post, modifier = Modifier.fillMaxSize(), onClick = {
            showDecorations = !showDecorations
        })
    }
}
