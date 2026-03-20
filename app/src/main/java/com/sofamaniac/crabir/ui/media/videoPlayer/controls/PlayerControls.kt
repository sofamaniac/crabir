package com.sofamaniac.crabir.ui.media.videoPlayer.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayerManager

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    fullscreenButton: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    val player = VideoPlayerManager.getInstance(context)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = Color.Black.copy(alpha = 0.3f))
    ) {
        PlayPauseButton(player = player, tint = Color.White)
        SeekProgressBar(
            player, Modifier
                .weight(1f)
                .fillMaxWidth(),
            sliderColors = SliderDefaults.colors().copy(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            )
        )
        PositionDurationTimeText(
            player = player,
            style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
        )
        fullscreenButton?.invoke()
    }
}