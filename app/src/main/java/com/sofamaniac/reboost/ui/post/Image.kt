/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.media.image.FromPreview

@Composable
fun PostImage(post: PostData, modifier: Modifier = Modifier) {
    if (post.getPreview()?.images?.isNotEmpty() == true) {
        FromPreview(post, modifier)
    } else {
        val url = post.url
        AsyncImage(
            model = url,
            contentDescription = post.title,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Fit,
        )
    }
}

