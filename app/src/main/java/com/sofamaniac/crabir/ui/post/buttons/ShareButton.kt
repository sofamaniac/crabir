package com.sofamaniac.crabir.ui.post.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.ListItem
import com.sofamaniac.crabir.ui.post.dialog.ShareMenu

@Composable
internal fun ShareButtonLong(post: PostData, closeMenu: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    ListItem(
        content = { Text(stringResource(R.string.share)) },
        modifier = Modifier.clickable { showMenu = true }
    )
    if (showMenu) {
        ShareMenu(post) {
            showMenu = false
        }
    }
}

@Composable
internal fun ShareButton(post: PostData) {
    var showMenu by remember { mutableStateOf(false) }
    IconButton(onClick = { showMenu = true }) {
        Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share))
    }
    if (showMenu) {
        ShareMenu(post) {
            showMenu = false
        }
    }
}
