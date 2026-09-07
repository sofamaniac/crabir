package com.sofamaniac.crabir.ui.post

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Gallery
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.FullscreenGalleryRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.onWifiConnection
import com.sofamaniac.crabir.settings.data.NetworkPolicy
import com.sofamaniac.crabir.ui.SaveToHistory
import com.sofamaniac.crabir.ui.components.cartouche
import com.sofamaniac.crabir.ui.media.FullscreenBottomBar
import com.sofamaniac.crabir.ui.media.FullscreenTopBar
import com.sofamaniac.crabir.ui.media.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.gallery.Gallery
import com.sofamaniac.crabir.ui.media.image.ImageView
import com.sofamaniac.crabir.ui.media.image.TransformableImage
import com.sofamaniac.crabir.ui.media.videoPlayer.DecoratedVideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayer
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostGallery(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
    blur: Boolean = false,
    goFullscreen: (Route) -> Unit,
) {
    val gallery = post.gallery
    if (gallery?.images.isNullOrEmpty()) {
        return
    }

    val state = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size })

    EmbeddedGallery(
        state,
        gallery,
        blur = blur,
        modifier = modifier
            .fillMaxSize(),
        goFullscreen = {
            goFullscreen(FullscreenGalleryRoute(post.name, page = state.currentPage))
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
    val dataSettings = LocalDataSettings.current
    val loadImage = when (dataSettings.imageQuality.loadImage) {
        NetworkPolicy.Always -> true
        NetworkPolicy.Never -> false
        NetworkPolicy.OnWifi -> LocalContext.current.onWifiConnection
    }

    if (!loadImage) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .clickable { goFullscreen() }) {
            UnloadedPlaceHolder(modifier.fillMaxSize())
            Text(
                stringResource(R.string.gallery),
                modifier = Modifier
                    .cartouche(Color.Black.copy(0.6f))
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = Color.White
            )
        }
        return
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
                val blurBackground = when (metadata) {
                    is MediaMetadata.Gif -> {
                        metadata.obfuscated.minByOrNull { it.width }?.toMediaResource()?.url
                    }

                    is MediaMetadata.Image -> {
                        metadata.obfuscated.minByOrNull { it.width }?.toMediaResource()?.url
                    }

                    else -> {
                        null
                    }
                }
                val backgroundUrl = when (metadata) {
                    is MediaMetadata.Gif -> {
                        val resource = metadata.preview.minByOrNull { it.width }?.toMediaResource()
                        resource?.url
                    }

                    is MediaMetadata.Image -> {
                        metadata.preview.minByOrNull { it.width }?.url
                    }

                    else -> null
                }
                if (blurBackground != null) {
                    AsyncImage(
                        blurBackground,
                        modifier = modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                } else if (backgroundUrl != null) {
                    // If available, blur background
                    TransformableImage(
                        source = backgroundUrl,
                        modifier = modifier.fillMaxSize(),
                        blur = true,
                        allowZoom = false,
                        contentScale = ContentScale.FillBounds
                    )
                }
                if (!blur) {
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
                                key = metadata.id ?: page.toString(),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                autostart = canPlayVideo && state.currentPage == page,
                                clickable = true,
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
                            Log.e(
                                "PostGallery",
                                "Unsupported media type: ${metadata.javaClass.name}"
                            )
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color(154, 154, 154, 255)
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Content not found"
                                )
                            }
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
    post: Fullname,
    initialPage: Int = 0,
    viewModel: PostDataViewModel = koinViewModel { parametersOf(post) },
    dismiss: () -> Unit,
    onPageChanged: (Int) -> Unit = {},
) {
    SaveToHistory(post)
    val post = viewModel.post.collectAsState(initial = null).value ?: return
    val gallery = post.gallery ?: return
    val state: PagerState =
        rememberPagerState(initialPage = initialPage, pageCount = { gallery.images.size })
    var showDecorations by remember { mutableStateOf(true) }
    val onClick = {
        showDecorations = !showDecorations
    }
    var showControls by remember { mutableStateOf(false) }
    var enableDismiss by remember { mutableStateOf(true) }
    LaunchedEffect(state.currentPage) {
        onPageChanged(state.currentPage)
        enableDismiss = true
        showControls = gallery.get(state.currentPage) is MediaMetadata.Gif
    }
    VerticalSwipeToDismiss(
        enabled = enableDismiss,
        topBar = {
            FullscreenTopBar(showDecorations) {
                Text(
                    "${state.currentPage + 1}/${state.pageCount}",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
                )
            }
        },
        bottomBar = {
            FullscreenBottomBar(post, showDecorations) {
                val title = gallery.images[state.currentPage].caption
                if (showControls) {
                    PlayerControls {
                        IconButton(onClick = dismiss) {
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
        onDismiss = dismiss,
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
                        onClick = onClick,
                        onZoomChange = { zoom ->
                            enableDismiss = zoom == 1f || zoom == 0f
                        }
                    )

                is MediaMetadata.Gif ->
                    VideoPlayer(
                        media = metadata.toMediaResource(),
                        key = metadata.id ?: page.toString(),
                        modifier = Modifier.fillMaxSize(),
                        startPlaying = state.currentPage == page,
                        placeholder = {
                            ImageView(
                                metadata.preview.last().toMediaResource(),
                                allowTransformation = false,
                                modifier = Modifier.fillMaxSize(),
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

