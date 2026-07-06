package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
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
    val navController = LocalNavController.current!!
    ThemedCard(
        modifier = modifier
            .depthIndent(more.depth.coerceAtLeast(0)),
        roundedCorners = false,
        onClick = {
            if (more.data.count > 0) {
                viewModel.fetchMoreComments(more)
            } else {
                val parentId = more.parentId.name.split('_').last()
                navController.navigate(
                    PostRoute(
                        postPermalink = viewModel.permalink,
                        comment = parentId,
                    )
                )
            }
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

fun LazyListScope.MoreNode(
    more: CommentType.More,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier
) {
    item {
        MoreViewer(more, viewModel, modifier)
    }
}