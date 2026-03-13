package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.ui.ThemedCard

@Composable
fun MoreViewer(
    more: CommentType.More,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val resources = LocalResources.current
    val text = resources.getQuantityString(
        R.plurals.MoreComments,
        more.data.count,
        more.data.count
    )
    ThemedCard(
        modifier = modifier,
        shape = RoundedCornerShape(0),
        onClick = {
            viewModel.fetchMoreComments(more)
        }
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleSmall,
            color = theme.highlight,
            modifier = Modifier.padding(8.dp)
        )
    }
}