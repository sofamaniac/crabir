package com.sofamaniac.crabir.ui.post.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenThreadButton(onClick: () -> Unit) {
    val description = stringResource(R.string.open_comments)
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above,
        ),
    ) {
        IconButton(onClick = onClick) {
            Icon(
                Icons.AutoMirrored.Filled.Comment,
                contentDescription = description,
            )
        }
    }
}
