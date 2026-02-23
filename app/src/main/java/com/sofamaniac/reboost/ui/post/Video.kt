/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.sofamaniac.reboost.domain.model.MediaResource
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.cartouche
import com.sofamaniac.reboost.ui.media.image.FromPreview
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayer

@OptIn(UnstableApi::class)
@Composable
fun PostVideo(post: PostData, modifier: Modifier = Modifier, canPlayVideo: Boolean = false) {
    val source = getVideoUrl(post)
    if (source != null) {
        VideoPlayer(
            source, startPlaying = canPlayVideo,
            placeholder = {
                PostImage(post)
            })
    } else if (post.media.media?.redditVideo != null) {
        val video = post.media.media.redditVideo.toMediaResource()
        VideoPlayer(
            video, startPlaying = canPlayVideo,
            placeholder = {
                PostImage(post)
            })
    } else {
        PostImage(post)
    }

}

@Composable
fun YoutubeVideo(post: PostData, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = {
                uriHandler.openUri(post.url)
            })
    ) {
        FromPreview(
            post,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            allowTransformation = false
        )
        Text(
            "Youtube",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .cartouche(Color.Red)
        )
    }
}


fun getVideoUrl(post: PostData): MediaResource? {
    val media =
        post.media.media?.redditVideo?.toMediaResource() ?: post.crosspostParentList.firstOrNull()
            ?.let { getVideoUrl(it) }
    if (media != null) {
        return media
    }
    return post.preview?.images?.firstOrNull()?.variants?.mp4?.source?.toMediaResource()
}


