package com.sofamaniac.crabir.ui.media.videoPlayer.controls

import androidx.annotation.OptIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.ui.compose.indicators.TimeText

@OptIn(UnstableApi::class)
@Composable
fun RemainingTimeText(player: Player, modifier: Modifier = Modifier, style: TextStyle? = null) {
    TimeText(player) {
        val remainingTime = this.durationMs - this.currentPositionMs
        val s = Util.getStringForTime(remainingTime)
        Text(
            s, style = style ?: LocalTextStyle.current,
            modifier = modifier
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PositionDurationTimeText(
    player: Player,
    modifier: Modifier = Modifier,
    style: TextStyle? = null
) {
    TimeText(player) {
        val remainingTime = this.currentPositionMs
        val remainder = Util.getStringForTime(remainingTime)
        val duration = Util.getStringForTime(this.durationMs)
        val s = "$remainder/$duration"
        Text(
            s, style = style ?: LocalTextStyle.current,
            modifier = modifier
        )
    }
}
