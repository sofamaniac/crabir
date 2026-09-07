package com.sofamaniac.crabir.ui.post.buttons

import androidx.compose.foundation.clickable
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
import com.sofamaniac.crabir.ui.post.dialog.MuteDialog

@Composable
internal fun MuteButton(post: PostData, onDismissRequest: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    ListItem(
        content = { Text(stringResource(R.string.mute)) },
        modifier = Modifier.clickable(onClick = { showMenu = true })
    )
    if (showMenu) {
        MuteDialog(post, onDismissRequest = { showMenu = false }) {
            showMenu = false
            onDismissRequest()
        }
    }
}
