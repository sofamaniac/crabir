package com.sofamaniac.reboost.ui.thread

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.domain.model.CommentType

@Composable
fun MoreViewer(commentsResponse: CommentType.More, modifier: Modifier = Modifier) {
    Text("More", modifier, style = MaterialTheme.typography.titleSmall)
}