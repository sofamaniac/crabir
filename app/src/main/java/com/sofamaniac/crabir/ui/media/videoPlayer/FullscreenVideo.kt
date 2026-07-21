package com.sofamaniac.crabir.ui.media.videoPlayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.ui.media.FullscreenBottomBar
import com.sofamaniac.crabir.ui.media.FullscreenTopBar
import com.sofamaniac.crabir.ui.media.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.image.DownloadButton
import com.sofamaniac.crabir.ui.media.videoPlayer.controls.PlayerControls
import com.sofamaniac.crabir.ui.post.PostDataViewModel
import com.sofamaniac.crabir.ui.post.getVideoUrl
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FullscreenVideo(
    post: Fullname,
    viewModel: PostDataViewModel = koinViewModel { parametersOf(post) },
    dismiss: () -> Unit,
) {
    val post = viewModel.post.collectAsState(initial = null).value ?: return
    var showDecorations by remember { mutableStateOf(true) }
    val video = getVideoUrl(post)!!
    VerticalSwipeToDismiss(
        topBar = {
            FullscreenTopBar(showDecorations, actions = { DownloadButton(video.url.toUri()) })
        },
        bottomBar = {
            FullscreenBottomBar(post, showDecorations) {
                PlayerControls {
                    IconButton(onClick = dismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.FullscreenExit,
                            contentDescription = "Exit fullscreen",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
        onDismiss = dismiss,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showDecorations = !showDecorations
            },
    ) {
        VideoPlayer(
            video,
            key = post.id,
            startPlaying = true,
            mute = false,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun FullscreenVideo(
    url: String,
    dismiss: () -> Unit,
) {

    val mediaResource = MediaResource(url, 1f, 1, 1)
    VerticalSwipeToDismiss(
        onDismiss = dismiss,
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoPlayer(
            mediaResource,
            key = url,
            startPlaying = true,
            mute = false,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
