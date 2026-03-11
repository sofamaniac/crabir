package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sofamaniac.crabir.domain.model.CommentType

@Composable
fun MoreViewer(
    more: CommentType.More,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier
) {
    Text(
        "More",
        style = MaterialTheme.typography.titleSmall,
        color = Color.Blue,
        modifier = modifier.clickable {
            Log.d("More", "Click")
            viewModel.fetchMoreComments(more)
        }
    )
}