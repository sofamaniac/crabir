/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.sofamaniac.reboost.domain.model.MediaResource
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayer

@OptIn(UnstableApi::class)
@Composable
fun PostVideo(post: PostData, modifier: Modifier = Modifier, canPlayVideo: Boolean = false) {
    val source = getVideoUrl(post)
    if (source != null) {
        VideoPlayer(source, startPlaying = canPlayVideo)
    } else {
        PostImage(post)
    }

}


fun getVideoUrl(post: PostData): MediaResource? {
    val media = post.media.media?.reddit_video?.toMediaResource()
    if (media != null) {
        return media
    }
    return post.getPreview()?.images?.firstOrNull()?.variants?.mp4?.source?.toMediaResource()
}


