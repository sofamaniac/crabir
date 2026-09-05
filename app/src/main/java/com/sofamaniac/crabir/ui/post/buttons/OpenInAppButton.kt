package com.sofamaniac.crabir.ui.post.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.post.ButtonsSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OpenInAppButton(
    post: PostData,
) {
    val uriHandler = LocalUriHandler.current
    val description = stringResource(R.string.open_in_app)
    TooltipBox(
        tooltip = { PlainTooltip { Text(description) } },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
    ) {
        IconButton(onClick = {
            uriHandler.openUri(post.url)
        }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, description, tint = Color.Gray)
        }
    }
}

@Composable
internal fun OpenInAppLong(post: PostData, buttonsSettings: ButtonsSettings) {
    if (!buttonsSettings.openInApp) {
        val uriHandler = LocalUriHandler.current
        ListItem(
            content = { Text(stringResource(R.string.open_in_app)) },
            modifier = Modifier.clickable {
                uriHandler.openUri(post.url)
            }
        )
    }
}
