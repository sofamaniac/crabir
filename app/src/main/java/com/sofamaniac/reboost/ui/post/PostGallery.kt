package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.Gallery
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.Cartouche
import com.sofamaniac.reboost.ui.VerticalSwipeToDismiss
import com.sofamaniac.reboost.ui.media.gallery.Gallery
import com.sofamaniac.reboost.ui.media.image.ImageView
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayer

@Composable
fun PostGallery(
    post: PostData,
    modifier: Modifier = Modifier,
    goFullscreen: (@Composable () -> Unit) -> Unit,
    dismiss: () -> Unit,
    canPlayVideo: Boolean = false,
) {
    val gallery = post.getGalleryData()
    if (gallery.images.isEmpty()) {
        return
    }

    val state = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size })

    val fullscreenView = @Composable {
        FullscreenGallery(
            post = post,
            gallery = gallery,
            state = state,
            dismiss = dismiss
        )
    }
    EmbeddedGallery(
        state,
        gallery,
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(gallery.aspectRatio)
            .clickable(onClick = {
                Log.d("PostGallery", "Clicking on gallery")
                goFullscreen { fullscreenView() }
            }),
        goFullscreen = { goFullscreen { fullscreenView() } },
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

    Box(modifier = modifier) {
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
                    VideoPlayer(
                        media = metadata.toMediaResource(),
                        modifier = Modifier.fillMaxSize(),
                        startPlaying = canPlayVideo && state.currentPage == page,
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
        Cartouche(
            backgroundColor = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(all = 4.dp)
        ) {
            Text(
                "${state.currentPage + 1}/${gallery.images.size}",
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullscreenGallery(
    post: PostData,
    state: PagerState,
    gallery: Gallery,
    dismiss: () -> Unit,
) {
    var showDecorations by remember { mutableStateOf(true) }
    VerticalSwipeToDismiss(
        onDismiss = dismiss,
        backgroundContent = @Composable {
            Column() {
                Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {}
            }
        },
    ) {
        Box() {
            Gallery(
                gallery,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showDecorations = !showDecorations },
                //.align (Alignment.CenterHorizontally),
                state = state,
            ) { metadata, page ->
                when (metadata) {
                    is MediaMetadata.Image -> ImageView(
                        metadata.toMediaResource(),
                        allowTransformation = true
                    )

                    is MediaMetadata.Gif -> VideoPlayer(
                        media = metadata.toMediaResource(),
                        modifier = Modifier.fillMaxSize(),
                        startPlaying = state.currentPage == page,
                        fullscreenButton = {
                            IconButton(onClick = dismiss) {
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
            FullscreenGalleryDecoration(post, state, showDecorations, dismiss = dismiss)
        }
    }
}

@Composable
fun BoxScope.FullscreenGalleryDecoration(
    post: PostData,
    state: PagerState,
    enabled: Boolean,
    dismiss: () -> Unit
) {


    AnimatedContent(
        targetState = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha = 0.6f))
            .safeContentPadding()
            .align(Alignment.TopCenter),
        transitionSpec = {
            (fadeIn() + slideInVertically()).togetherWith(fadeOut() + slideOutVertically())
        },
        label = "decoration animation"
    ) { enabled ->
        if (!enabled) {
            return@AnimatedContent
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            IconButton(onClick = dismiss) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = Color.White
                )
            }
            Text(
                "${state.currentPage + 1}/${state.pageCount}",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
            )
        }
    }
    AnimatedContent(
        targetState = enabled,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .safeContentPadding(),
        transitionSpec = {
            (fadeIn() + slideInVertically()).togetherWith(fadeOut() + slideOutVertically())
        },
        label = "decoration bottom animation"
    ) { enabled ->
        if (!enabled) {
            return@AnimatedContent
        }
        Text(
            post.title,
            style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
        )
    }
}