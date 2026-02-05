package com.sofamaniac.reboost.ui.media.videoPlayer.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.indicator.PositionAndDurationText
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayerManager

@Composable
fun BoxScope.Controls(
    modifier: Modifier = Modifier,
    fullscreenButton: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    val player = VideoPlayerManager.getInstance(context)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        PlayPauseButton(player = player)
        SeekProgressBar(player, Modifier.fillMaxWidth(0.75f))
        PositionAndDurationText(player = player)
        fullscreenButton?.invoke()
    }
}