package com.sofamaniac.reboost.ui.post

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalFullscreenHandler
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.Gallery
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.VerticalSwipeToDismiss
import com.sofamaniac.reboost.ui.cartouche
import com.sofamaniac.reboost.ui.media.gallery.Gallery
import com.sofamaniac.reboost.ui.media.image.ImageView
import com.sofamaniac.reboost.ui.media.videoPlayer.DecoratedVideoPlayer

@Composable
fun PostGallery(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
) {
    val gallery = post.gallery
    if (gallery?.images.isNullOrEmpty()) {
        return
    }

    val state = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size })

    val fullscreenView = @Composable {
        FullscreenGallery(
            post = post,
            gallery = gallery,
            state = state,
        )
    }
    val fullscreenManager = LocalFullscreenHandler.current!!
    EmbeddedGallery(
        state,
        gallery,
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(gallery.aspectRatio),
        goFullscreen = {
            fullscreenManager.push { fullscreenView() }
        },
        canPlayVideo = canPlayVideo
    )
}

@Composable
fun EmbeddedGallery(
    state: PagerState,
    gallery: Gallery,
    goFullscreen: () -> Unit,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
) {
    val media = gallery.images[state.currentPage]
    Box(modifier = modifier.clickable { goFullscreen() }) {
        Gallery(
            gallery,
            modifier.aspectRatio(gallery.aspectRatio),
            state,
        ) { metadata, page ->
            when (metadata) {
                is MediaMetadata.Image -> ImageView(
                    metadata.toMediaResource(),
                    allowTransformation = false,
                )

                is MediaMetadata.Gif ->
                    DecoratedVideoPlayer(
                        media = metadata.toMediaResource(),
                        modifier = Modifier.fillMaxSize(),
                        startPlaying = canPlayVideo && state.currentPage == page,
                        placeholder = {
                            val resource = metadata.preview.lastOrNull()?.toMediaResource()
                            if (resource != null) {
                                ImageView(
                                    resource,
                                    allowTransformation = false,
                                )
                            }
                        },
                        fullscreenButton = {
                            IconButton(onClick = goFullscreen) {
                                Icon(
                                    Icons.Default.Fullscreen,
                                    contentDescription = "Go Fullscreen"
                                )
                            }
                        }
                    )


                else -> {
                    Text("No luck my friend (${metadata.javaClass.simpleName})")
                }

            }
        }
        Text(
            "${state.currentPage + 1}/${gallery.images.size}",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(all = 4.dp)
                .cartouche(Color.Black.copy(alpha = 0.6f))
        )
        if (media.caption != null) {
            Text(
                media.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(0.6f))
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullscreenGallery(
    post: PostData,
    state: PagerState = rememberPagerState(pageCount = { gallery.images.size }),
    gallery: Gallery,
) {
    var showDecorations by remember { mutableStateOf(true) }
    val fullscreenManager = LocalFullscreenHandler.current!!
    val onClick = {
        showDecorations = !showDecorations
    }
    VerticalSwipeToDismiss(
        topBar = {
            FullscreenTopBar(showDecorations) {
                Text(
                    "${state.currentPage + 1}/${state.pageCount}",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Companion.White)
                )
            }
        },
        bottomBar = {
            FullscreenBottomBar(post, showDecorations) {
                val title = gallery.images[state.currentPage].caption
                if (title != null) {
                    Text(
                        title,
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick()
            },
    ) {
        Gallery(
            gallery,
            modifier = Modifier
                .fillMaxSize(),
            state = state,
        ) { metadata, page ->
            when (metadata) {
                is MediaMetadata.Image -> ImageView(
                    metadata.toMediaResource(),
                    allowTransformation = true,
                    onClick = onClick
                )

                is MediaMetadata.Gif -> DecoratedVideoPlayer(
                    media = metadata.toMediaResource(),
                    modifier = Modifier.fillMaxSize(),
                    startPlaying = state.currentPage == page,
                    placeholder = {
                        ImageView(
                            metadata.preview.last().toMediaResource(),
                            allowTransformation = false,
                        )
                    },
                    fullscreenButton = {
                        IconButton(onClick = fullscreenManager::pop) {
                            Icon(
                                Icons.Default.FullscreenExit,
                                contentDescription = "Exit Fullscreen"
                            )
                        }
                    }
                )


                else -> {
                    Text("No luck my friend (${metadata.javaClass.simpleName})")
                }
            }
        }
    }
}

