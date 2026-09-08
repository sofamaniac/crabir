package com.sofamaniac.crabir.ui.thread.topBar

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.comments.CommentsSettingsRoute

@Composable
fun MoreOptionsMenu(
    refresh: () -> Unit,
    goToCommunityInfo: () -> Unit,
    share: () -> Unit,
    reply: () -> Unit,
) {
    val navController = LocalNavController.current
    var showMenu by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { showMenu = true }) {
            Icon(Icons.Default.MoreVert, stringResource(R.string.more_option_desc))
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(text = { Text(stringResource(R.string.reply)) }, onClick = reply)
            DropdownMenuItem(
                text = { Text(stringResource(R.string.refresh)) },
                onClick = { refresh() }
            )
            DropdownMenuItem(text = { Text(stringResource(R.string.share)) }, onClick = share)
            DropdownMenuItem(
                text = { Text(stringResource(R.string.community_info)) },
                onClick = goToCommunityInfo
            )
            DropdownMenuItem(text = { Text(stringResource(R.string.settings)) }, onClick = {
                navController?.navigate(
                    CommentsSettingsRoute
                )
            })
        }
    }
}
