/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.sofamaniac.reboost.data.remote.dto.post.PostImageSource
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.videoPlayer.VideoPlayer

@OptIn(UnstableApi::class)
@Composable
fun PostVideo(post: PostData, modifier: Modifier = Modifier) {
    val source = getVideoUrl(post)
    if (source != null) {
        VideoPlayer(source)
    } else {
        PostImage(post)
    }

}


fun getVideoUrl(post: PostData): PostImageSource? {
    val media = post.media.media?.reddit_video
    if (media != null) {
        return PostImageSource(url = media.fallback_url, width = media.width, height = media.height)
    }
    return post.getPreview()?.images?.firstOrNull()?.variants?.mp4?.source
}


