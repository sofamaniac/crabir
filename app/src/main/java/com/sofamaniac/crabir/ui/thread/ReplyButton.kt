package com.sofamaniac.crabir.ui.thread

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyButton(startReply: () -> Unit) {
    IconButton(onClick = {
        startReply()
    }) {
        Icon(Icons.AutoMirrored.Default.Reply, contentDescription = "Reply")
    }
}
