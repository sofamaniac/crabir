package com.sofamaniac.crabir.ui.thread

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sofamaniac.crabir.domain.model.Fullname

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyButton(parent: Fullname, viewModel: CommentViewModelInterface) {
    IconButton(onClick = {
        viewModel.replyTo(parent)
    }) {
        Icon(Icons.AutoMirrored.Default.Reply, contentDescription = "Reply", tint = Color.Gray)
    }
}