package com.sofamaniac.reboost.ui.media.videoPlayer.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.ui.compose.material3.buttons.MuteButton
import com.sofamaniac.reboost.ui.cartouche

@Composable
fun AlwaysOnInfo(player: Player, modifier: Modifier = Modifier) {
    val backgroundColor = Color.Black.copy(alpha = 0.66f)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        MuteButton(player, modifier = Modifier.cartouche(backgroundColor))
        RemainingTimeText(
            player, style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.cartouche(backgroundColor)
        )
    }
}