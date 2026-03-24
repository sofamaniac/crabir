package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.CommentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val theme = LocalTheme.current

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize()
    ) {
        val comments by viewModel.comments
            .collectAsState(initial = emptyList())
        val post by viewModel.post.collectAsState(initial = null)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Show post
            item {
                if (post != null) {
                    PostView(post!!, threadViewModel = viewModel)
                    HorizontalDivider()
                }
            }
            if (comments.isEmpty() && !isRefreshing) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "No comments",
                        )
                    }
                }
            }
            items(comments.size, key = { index -> comments[index].name }) { index ->
                when (val comment = comments[index]) {
                    is CommentType.Comment -> CommentNode(
                        comment.comment,
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = theme.cardBackground)
                            .depthIndent(comment.depth.coerceAtLeast(0), color = Color.Gray)
                    )

                    is CommentType.More -> MoreViewer(
                        comment,
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = theme.cardBackground)
                            .depthIndent(comment.depth, color = Color.Gray)
                    )
                }
                HorizontalDivider()
            }
            item {
                Spacer(modifier = Modifier.fillParentMaxHeight(0.1f))
            }
        }
    }
}

fun List<CommentType>.flattenComments(): List<CommentType> {
    val comments = emptyList<CommentType>().toMutableList()
    for (comment in this) {
        comments += comment
        if (comment is CommentType.Comment) {
            if (!comment.comment.collapsed) {
                comments += comment.comment.replies.flattenComments()
            }
        }
    }
    return comments
}