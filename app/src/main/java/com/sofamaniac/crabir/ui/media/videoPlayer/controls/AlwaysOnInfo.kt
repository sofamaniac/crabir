package com.sofamaniac.crabir.ui.media.videoPlayer.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.ui.compose.material3.buttons.MuteButton
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayerManager

@Composable
fun AlwaysOnInfo(modifier: Modifier = Modifier) {
    val backgroundColor = Color.Black.copy(alpha = 0.66f)
    val context = LocalContext.current
    val player = VideoPlayerManager.getInstance(context)
    val showMuteButton by VideoPlayerManager.hasAudio.collectAsState(false)
    Row(
        horizontalArrangement = if (showMuteButton) Arrangement.SpaceBetween else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (showMuteButton) {
            MuteButton(
                player,
                modifier = Modifier
                    .cartouche(backgroundColor)
                    .size(32.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
            )
        }
        RemainingTimeText(
            player,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            modifier = Modifier
                .height(32.dp)
                .cartouche(backgroundColor)
                .wrapContentHeight(align = Alignment.CenterVertically),
        )
    }
}
