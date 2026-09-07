package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.components.RefreshIndicator
import com.sofamaniac.crabir.ui.components.ThemedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    comment: String? = null,
    context: Int? = null,
) {
    val listState = viewModel.listState
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val theme = LocalTheme.current
    val navController = LocalNavController.current!!
    val refreshBoxState = rememberPullToRefreshState()
    val commentsSettings = LocalCommentsSettings.current

    PullToRefreshBox(
        state = refreshBoxState,
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize(),
        indicator = {
            RefreshIndicator(isRefreshing, state = refreshBoxState)
        }
    ) {
        val comments by viewModel.comments.collectAsState()
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
                    ThemedCard(
                        //modifier = modifier,
                        roundedCorners = false,
                        onClick = {
                            navController.navigate(PostRoute(postPermalink = viewModel.permalink))
                        }
                    ) {
                        Text(
                            stringResource(R.string.see_full_thread),
                            style = MaterialTheme.typography.titleSmall,
                            color = theme.highlight,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    HorizontalDivider()
                }
            }
            item {
                if (!comment.isNullOrBlank() && context == null) {
                    ThemedCard(
                        //modifier = modifier,
                        roundedCorners = false,
                        onClick = {
                            navController.navigate(
                                PostRoute(
                                    postPermalink = viewModel.permalink,
                                    comment = comment,
                                    context = 8
                                )
                            )
                        }
                    ) {
                        Text(
                            stringResource(R.string.show_full_context),
                            style = MaterialTheme.typography.titleSmall,
                            color = theme.highlight,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    HorizontalDivider()
                }
            }
            if (!comments.any() && !isRefreshing) {
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
                            stringResource(R.string.no_comments),
                        )
                    }
                }
            }

            items(
                items = comments,
                key = { it.name }
            ) { comment ->
                when (comment) {
                    is CommentType.Comment -> {
                        CommentNode(
                            comment = comment,
                            viewModel = viewModel,
                            enableAnimation = !commentsSettings.buttonsAlwaysVisible,
                        )
                    }

                    is CommentType.More -> {
                        MoreViewer(
                            more = comment,
                            viewModel = viewModel
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.fillParentMaxHeight(0.1f))
            }
        }
    }
}

