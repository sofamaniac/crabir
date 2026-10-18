package com.sofamaniac.crabir.ui.thread

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyButton(enabled: Boolean, startReply: () -> Unit) {
    val snackbarHost = LocalSnackBarHost.current
    val scope = rememberCoroutineScope()
    val cantReplyMessage = stringResource(R.string.cannot_reply_to_message)
    IconButton(
        onClick = {
            if (enabled) {
                startReply()
            } else {
                scope.launch {
                    snackbarHost?.showSnackbar(cantReplyMessage)
                }
            }
        }
    ) {
        Icon(Icons.AutoMirrored.Default.Reply, contentDescription = "Reply")
    }
}
