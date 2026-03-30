package com.sofamaniac.crabir.ui.post

import android.os.Build
import android.util.Log
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.Gallery
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.rememberFiltersSettings
import com.sofamaniac.crabir.ui.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.media.gallery.Gallery
import com.sofamaniac.crabir.ui.media.image.ImageView
import com.sofamaniac.crabir.ui.media.videoPlayer.DecoratedVideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls

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

    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberPagerState(initialPage = currentPage, pageCount = { gallery.images.size })

    LaunchedEffect(state.currentPage) {
        currentPage = state.currentPage
    }

    val fullscreenManager = LocalFullscreenHandler.current!!
    val filters = rememberFiltersSettings()
    val blur = post.spoiler || (post.over18 && filters.blurNSFW)
    EmbeddedGallery(
        state,
        gallery,
        blur = blur,
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(gallery.aspectRatio),
        goFullscreen = {
            fullscreenManager.push {
                FullscreenGallery(
                    post = post,
                    gallery = gallery,
                    initialPage = currentPage,
                    onPageChanged = { currentPage = it }
                )
            }
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
    blur: Boolean = false,
) {
    val media = gallery.images[state.currentPage]
    val innerModifier = if (blur) {
        modifier.blur(40.dp)
    } else {
        modifier
    }


    Box(
        modifier = Modifier
            .clickable { goFullscreen() }
            .aspectRatio(gallery.aspectRatio)
    ) {
        Gallery(
            gallery,
            modifier.aspectRatio(gallery.aspectRatio),
            state,
            enableScroll = !blur,
        ) { metadata, page ->
            Box {
                val backgroundUrl = when (metadata) {
                    is MediaMetadata.Gif -> {
                        val resource = metadata.preview.lastOrNull()?.toMediaResource()
                        resource?.url
                    }

                    is MediaMetadata.Image -> {
                        metadata.preview!!.last().url
                    }

                    else -> null
                }
                if (backgroundUrl != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AsyncImage(
                        backgroundUrl,
                        modifier = innerModifier
                            .fillMaxSize()
                            .blur(40.dp),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                    )
                }
                when (metadata) {
                    is MediaMetadata.Image -> ImageView(
                        metadata.toMediaResource(),
                        allowTransformation = false,
                        modifier = innerModifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                    )

                    is MediaMetadata.Gif ->
                        DecoratedVideoPlayer(
                            media = metadata.toMediaResource(),
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            startPlaying = canPlayVideo && state.currentPage == page && !blur,
                            clickable = !blur,
                            placeholder = {
                                val resource = metadata.preview.lastOrNull()?.toMediaResource()
                                if (resource != null) {
                                    ImageView(
                                        resource,
                                        modifier = innerModifier.fillMaxSize(),
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
                        Log.e("PostGallery", "Unsupported media type: ${metadata.javaClass.name}")
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color(154, 154, 154, 255)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = "Content not found")
                        }
                    }
                }
            }
        }
        val text = if (blur) {
            "${gallery.images.size} images"
        } else {
            "${state.currentPage + 1}/${gallery.images.size}"
        }
        Text(
            text,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(all = 4.dp)
                .cartouche(
                    Color.Black.copy(alpha = 0.6f)
                )
        )
        if (!media.caption.isNullOrBlank()) {
            Text(
                media.caption,
                color = Color.White,
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
    gallery: Gallery,
    initialPage: Int = 0,
    onPageChanged: (Int) -> Unit = {},
) {
    val state: PagerState =
        rememberPagerState(initialPage = initialPage, pageCount = { gallery.images.size })
    var showDecorations by remember { mutableStateOf(true) }
    val fullscreenManager = LocalFullscreenHandler.current!!
    val onClick = {
        showDecorations = !showDecorations
    }
    var showControls by remember { mutableStateOf(false) }
    LaunchedEffect(state.currentPage) {
        onPageChanged(state.currentPage)
        showControls = gallery.get(state.currentPage) is MediaMetadata.Gif
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
                if (showControls) {
                    PlayerControls() {
                        IconButton(onClick = fullscreenManager::pop) {
                            Icon(
                                Icons.Default.FullscreenExit,
                                contentDescription = "Exit Fullscreen"
                            )
                        }
                    }
                }
                if (!title.isNullOrBlank()) {
                    Text(
                        title,
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        onDismiss = { fullscreenManager.pop() },
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
                is MediaMetadata.Image ->
                    ImageView(
                        metadata.toMediaResource(),
                        modifier = Modifier.fillMaxSize(),
                        allowTransformation = true,
                        onClick = onClick
                    )

                is MediaMetadata.Gif ->
                    VideoPlayer(
                        media = metadata.toMediaResource(),
                        modifier = Modifier.fillMaxSize(),
                        startPlaying = state.currentPage == page,
                        placeholder = {
                            ImageView(
                                metadata.preview.last().toMediaResource(),
                                allowTransformation = false,
                            )
                        },
                    )


                else -> {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(154, 154, 154, 255)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Content not found")
                    }
                }
            }
        }
    }
}

