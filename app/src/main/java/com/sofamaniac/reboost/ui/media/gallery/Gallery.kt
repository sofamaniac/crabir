package com.sofamaniac.reboost.ui.media.gallery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.Gallery

@Composable
fun Gallery(
    gallery: Gallery, modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size }),
    content: @Composable (MediaMetadata, page: Int) -> Unit,
) {
    HorizontalPager(state = state, modifier = Modifier.fillMaxSize()) { page ->
        val mediaId = gallery.images[page].mediaId
        val metadata: MediaMetadata? = gallery.mediaMetadata[mediaId]
        if (metadata != null) {
            content(metadata, page)
        }
    }
}