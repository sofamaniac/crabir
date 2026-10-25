package com.sofamaniac.crabir.ui.media.videoPlayer

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.isVideoUrl
import com.sofamaniac.crabir.settings.history.SaveToHistory
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
    val video = remember { getVideoUrl(post)!! }
    val downloadUrl = post.getDownloadUrl()
    SaveToHistory(post.name, post.over18)
    VerticalSwipeToDismiss(
        topBar = {
            FullscreenTopBar(
                showDecorations,
                actions = {
                    if (downloadUrl != null) {
                        DownloadButton(
                            downloadUrl,
                            subreddit = post.subreddit.name,
                        )
                    }
                }
            )
        },
        bottomBar = {
            FullscreenBottomBar(post, showDecorations) {
                PlayerControls()
            }
        },
        onDismiss = dismiss,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                showDecorations = !showDecorations
            },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            VideoPlayer(
                video,
                key = post.id,
                startPlaying = true,
                mute = false,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
            if (showDecorations) {
                PlayerButtons(
                    modifier = Modifier.align(Alignment.Center),
                    goFullscreen = dismiss,
                    fullscreenButtonIcon = Icons.Default.FullscreenExit,
                    fullscreenButtonContentDescription = stringResource(
                        R.string.video_exit_fullscreen
                    )
                )
            }
        }
    }
}

@Composable
fun FullscreenVideo(
    url: String,
    dismiss: () -> Unit,
) {

    VerticalSwipeToDismiss(
        onDismiss = dismiss,
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoPlayer(
            url,
            key = url,
            startPlaying = true,
            mute = false,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

fun PostData.getDownloadUrl(): Uri? {
    val default = getVideoUrl(this)?.url?.toUri()
    if (default == null || !default.isVideoUrl()) {
        return this.preview?.redditVideoPreview?.fallbackUrl?.toUri()
    }
    return default
}
