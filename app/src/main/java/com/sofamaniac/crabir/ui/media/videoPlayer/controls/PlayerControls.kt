package com.sofamaniac.crabir.ui.media.videoPlayer.controls

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.indicator.DurationText
import androidx.media3.ui.compose.material3.indicator.PositionText
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayerManager

@OptIn(UnstableApi::class)
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    fullscreenButton: @Composable (() -> Unit)? = null,
) {
    val player = VideoPlayerManager.getInstance()
    var showQualityMenu by remember { mutableStateOf(false) }
    var currentTracks by remember { mutableStateOf(player.currentTracks) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onTracksChanged(tracks: Tracks) {
                currentTracks = tracks
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = Color.Black.copy(alpha = 0.3f))
    ) {
        PlayPauseButton(player = player, tint = Color.White)
        PositionText(player = player, color = Color.White)
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
        if (currentTracks.groups.any { it.type == C.TRACK_TYPE_VIDEO }) {
            Box {
                IconButton(onClick = { showQualityMenu = true }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(R.string.change_quality),
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = showQualityMenu,
                    onDismissRequest = { showQualityMenu = false }
                ) {
                    val isAuto = VideoPlayerManager.isAuto()

                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.quality_auto)) },
                        trailingIcon = {
                            if (isAuto) {
                                Icon(Icons.Default.Check, contentDescription = null)
                            }
                        },
                        onClick = {
                            VideoPlayerManager.setAutoTrack()
                            showQualityMenu = false
                        }
                    )
                    for (group in currentTracks.groups.filter { it.type == C.TRACK_TYPE_VIDEO }) {
                        for (i in 0 until group.length) {
                            val format = group.getTrackFormat(i)
                            if (format.height > 0) {
                                val isSelected = group.isTrackSelected(i)
                                DropdownMenuItem(
                                    text = { Text(text = "${format.height}p") },
                                    trailingIcon = {
                                        if (isSelected && !isAuto) {
                                            Icon(Icons.Default.Check, contentDescription = null)
                                        }
                                    },
                                    onClick = {
                                        VideoPlayerManager.setTrack(group, i)
                                        showQualityMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        DurationText(player = player, color = Color.White)
        fullscreenButton?.invoke()
    }
}