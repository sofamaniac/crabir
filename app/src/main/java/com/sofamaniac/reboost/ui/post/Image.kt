/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.FullscreenManager
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.VerticalSwipeToDismiss
import com.sofamaniac.reboost.ui.media.image.FromPreview

@Composable
fun PostImage(
    post: PostData,
    modifier: Modifier = Modifier,
) {
    val goFullscreen = { view: @Composable BoxScope.() -> Unit ->
        FullscreenManager.push {
            VerticalSwipeToDismiss(
                modifier = modifier.fillMaxSize(),
                backgroundContent = @Composable {
                    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {}
                },
            ) {
                Box() {
                    view()
                }
            }
        }
    }
    if (post.preview?.images?.isNotEmpty() == true) {
        FromPreview(
            post,
            modifier = modifier.clickable {
                Log.d("PostImage", "Click on post image")
                goFullscreen {
                    FromPreview(
                        post,
                        modifier = modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        allowTransformation = true
                    )
                }
            },
            allowTransformation = false
        )
    } else {
        val url = post.url
        val view = @Composable { modifier: Modifier ->
            AsyncImage(
                model = url,
                contentDescription = post.title,
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Fit,
            )
        }
        view(Modifier.clickable {
            goFullscreen {
                view(Modifier)
            }
        })
    }
}

