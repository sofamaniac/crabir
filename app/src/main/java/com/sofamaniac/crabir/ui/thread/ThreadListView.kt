package com.sofamaniac.crabir.ui.thread

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.PostRoute
import com.sofamaniac.crabir.ui.components.RefreshIndicator
import com.sofamaniac.crabir.ui.components.ThemedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadListView(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    comment: String? = null,
    context: Int? = null,
) {
    val uiState by viewModel.loadingState.collectAsState()
    val refreshBoxState = rememberPullToRefreshState()
    val commentsSettings = LocalCommentsSettings.current
    val isLoading = uiState == LoadingState.Loading

    PullToRefreshBox(
        state = refreshBoxState,
        isRefreshing = isLoading,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize(),
        indicator = {
            RefreshIndicator(isLoading, state = refreshBoxState)
        }
    ) {
        val listState = viewModel.listState
        val comments by viewModel.comments.collectAsState()
        val openedComment by viewModel.openComment.collectAsState()
        val newComments by viewModel.newComments.collectAsState()
        val post by viewModel.post.collectAsState()

        if (post == null) return@PullToRefreshBox
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Show post
            item {
                PostView(
                    post!!,
                    threadViewModel = viewModel
                )
                HorizontalDivider()
            }
            item {
                if (!comment.isNullOrBlank()) {
                    FullThreadCard(
                        viewModel.permalink,
                        R.string.see_full_thread,
                        comment = null,
                    )
                }
            }
            item {
                if (!comment.isNullOrBlank() && context == null) {
                    FullThreadCard(
                        viewModel.permalink,
                        R.string.show_full_context,
                        comment = viewModel.comment,
                        context = 8
                    )
                }
            }
            item {
                if (!comments.any() && !isLoading) {
                    NoComments()
                }
            }

            items(
                items = comments,
                key = { it.name }
            ) { comment ->
                when (val c = comment) {
                    is CommentType.Comment -> {
                        val children = remember(comment.name) {
                            viewModel.getNumberOfChildren(comment.name)
                        }
                        CommentNode(
                            depth = comment.depth,
                            comment = c,
                            enableAnimation = !commentsSettings.buttonsAlwaysVisible,
                            opened = openedComment == comment.name,
                            isNew = newComments.contains(comment.name),
                            onEvent = viewModel::onEvent,
                            children = children
                        )
                    }

                    is CommentType.More -> {
                        MoreViewer(
                            more = c,
                            depth = comment.depth,
                            fetchMore = viewModel::fetchMoreComments,
                            permalink = viewModel.permalink,
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.fillParentMaxHeight(0.3f))
            }
        }
    }
}

@Composable
private fun FullThreadCard(
    permalink: String,
    @StringRes text: Int,
    comment: String?,
    context: Int? = null,
) {
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    ThemedCard(
        //modifier = modifier,
        roundedCorners = false,
        onClick = {
            navController?.navigate(
                PostRoute(
                    postPermalink = permalink,
                    comment = comment,
                    context = context
                )
            )
        }
    ) {
        Text(
            stringResource(text),
            style = MaterialTheme.typography.titleSmall,
            color = theme.highlight,
            modifier = Modifier.padding(8.dp)
        )
    }
    HorizontalDivider()
}

@Composable
private fun NoComments() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            stringResource(R.string.no_comments),
        )
    }
}
