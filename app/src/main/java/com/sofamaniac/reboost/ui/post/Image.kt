/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.LocalFullscreenHandler
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.VerticalSwipeToDismiss
import com.sofamaniac.reboost.ui.media.image.ImageView

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
    ImageView(
        post,
        modifier = modifier.clickable(enabled = enabled) {
            Log.d("PostImage", "Click on post image")
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
