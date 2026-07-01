package com.sofamaniac.crabir.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.data.remote.streamable.StreamableAPI
import com.sofamaniac.crabir.data.remote.streamable.Video
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.FullscreenVideoRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.filters.rememberFiltersSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

@KoinViewModel
class StreamableViewModel(
    @InjectedParam post: PostData,
    api: StreamableAPI
) : ViewModel() {
    private val _video: MutableStateFlow<Video?> = MutableStateFlow(null)
    val video: StateFlow<Video?> = _video.asStateFlow()

    private val _thumbnailUrl: MutableStateFlow<String?> = MutableStateFlow(null)
    val thumbnailUrl: StateFlow<String?> = _thumbnailUrl.asStateFlow()

    init {
        val id = post.url.toUri().lastPathSegment
        if (id != null) {
            viewModelScope.launch {
                val response = api.getVideo(id)
                if (response.isSuccessful) {
                    val body = response.body() ?: return@launch
                    _video.value = body.files.mp4
                    _thumbnailUrl.value = body.thumbnailUrl
                }
            }
        }
    }
}

@Composable
fun StreamableVideo(
    post: PostData,
    canPlayVideo: Boolean,
    modifier: Modifier = Modifier,
    goFullscreen: (Route) -> Unit,
    viewModel: StreamableViewModel = koinViewModel { parametersOf(post) }
) {
    val video by viewModel.video.collectAsState()
    val thumbnailUrl by viewModel.thumbnailUrl.collectAsState()
    val filters = rememberFiltersSettings()
    val blur = post.spoiler || (post.over18 && filters.blurNSFW)
    if (video == null) return
    val goFullscreen = {
        goFullscreen(FullscreenVideoRoute(post.name))
    }
    PostVideo(
        video!!.toMediaResource(),
        canPlayVideo = canPlayVideo,
        blur = blur,
        modifier = modifier.clickable(enabled = blur) { goFullscreen() },
        goFullscreen = goFullscreen,
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Fit,
        )
    }

}