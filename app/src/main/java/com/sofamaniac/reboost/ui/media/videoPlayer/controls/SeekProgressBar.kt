package com.sofamaniac.reboost.ui.media.videoPlayer.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekProgressBar(player: Player, modifier: Modifier = Modifier) {
    var sliderState by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(player) {
        while (true) {
            sliderState = player.currentPosition.toFloat() / player.duration.toFloat()
            delay(100)
        }
    }

    Slider(
        value = sliderState,
        onValueChange = {
            sliderState = it
            player.seekTo((it * player.duration).toLong())
        },
        modifier = modifier,
        thumb = {
            Box(
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            24.dp
                        )
                    )
                    .size(
                        24.dp
                    )
                    .background(color = MaterialTheme.colorScheme.primary),
            )
        },
        track = {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .height(
                        8.dp
                    )
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
            )
        },
    )
}