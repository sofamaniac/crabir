package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.Cartouche
import com.sofamaniac.reboost.ui.media.Gallery

@Composable
fun PostGallery(post: PostData, modifier: Modifier = Modifier) {
    val gallery = post.getGalleryData()
    if (gallery.images.isEmpty()) {
        return
    }

    val state = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size })
    var fullscreen by remember { mutableStateOf(false) }
    Gallery(gallery, modifier, state, onTap = { fullscreen = true }) {
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
    if (fullscreen) {
        val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
        Dialog(
            onDismissRequest = { fullscreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            SwipeToDismissBox(
                state = swipeToDismissBoxState,
                backgroundContent = {},
                onDismiss = { fullscreen = false }
            ) {
                Gallery(
                    gallery,
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    state,
                    onTap = null
                )

            }
        }
    }
}