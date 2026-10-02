package com.sofamaniac.crabir.ui.media.videoPlayer.controls

import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player
import androidx.media3.ui.compose.material3.indicator.ProgressSlider

@Composable
@androidx.media3.common.util.ExperimentalApi
fun SeekProgressBar(
    player: Player, modifier: Modifier = Modifier,
    sliderColors: SliderColors = SliderDefaults.colors(),
) {
    ProgressSlider(player, modifier = modifier, colors = sliderColors)
}
