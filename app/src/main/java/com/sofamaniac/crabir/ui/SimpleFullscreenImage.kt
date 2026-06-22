package com.sofamaniac.crabir.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.media.image.DownloadButton
import com.sofamaniac.crabir.ui.post.FullscreenTopBar
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage

@Composable
fun SimpleFullscreenImage(url: String) {
    val navController = LocalNavController.current
    val url = "https://i.redd.it/$url"
    var showOverlay by remember { mutableStateOf(true) }

    VerticalSwipeToDismiss(
        topBar = { ->
            FullscreenTopBar(enabled = showOverlay, actions = {
                DownloadButton(url.toUri())
            })
        },
        onDismiss = {
            navController?.popBackStack()
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        ZoomableAsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}