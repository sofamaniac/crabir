package com.sofamaniac.crabir.ui.media.gallery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.Gallery

@Composable
fun Gallery(
    gallery: Gallery,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size }),
    enableScroll: Boolean = true,
    content: @Composable (MediaMetadata, page: Int) -> Unit,
) {
    HorizontalPager(
        state = state,
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = enableScroll,
    ) { page ->
        val metadata: MediaMetadata? = gallery.get(page)
        if (metadata != null) {
            content(metadata, page)
        }
    }
}
