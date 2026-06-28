package com.sofamaniac.crabir.ui.thread

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(Icons.AutoMirrored.Default.Reply, contentDescription = "Reply", tint = Color.Gray)
    }
}