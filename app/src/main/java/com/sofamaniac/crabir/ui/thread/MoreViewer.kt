package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.components.ThemedCard

@Composable
fun MoreViewer(
    more: CommentType.More,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val resources = LocalResources.current
    val text = if (more.data.count == 0) stringResource(R.string.see_more_comments) else
        resources.getQuantityString(
            R.plurals.MoreComments,
            more.data.count,
            more.data.count
        )
    val navController = LocalNavController.current!!
    var loading by remember { mutableStateOf(false) }
    ThemedCard(
        modifier = modifier
            .background(theme.cardBackground)
            .depthIndent(more.depth.coerceAtLeast(0))
            .fillMaxWidth(),
        roundedCorners = false,
        onClick = {
            if (loading) return@ThemedCard
            if (more.data.count > 0) {
                loading = true
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.titleSmall,
                color = theme.highlight,
            )
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            }
        }
    }
}

fun LazyListScope.moreNode(
    more: CommentType.More,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    item {
        MoreViewer(more, viewModel, modifier)
    }
}
