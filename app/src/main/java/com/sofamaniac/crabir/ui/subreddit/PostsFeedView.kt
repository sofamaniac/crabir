/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:18 PM
 *
 */

package com.sofamaniac.crabir.ui.subreddit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.views.Views
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.HorizontalSwipeToDismiss
import com.sofamaniac.crabir.ui.SortMenu
import com.sofamaniac.crabir.ui.post.CompactView
import com.sofamaniac.crabir.ui.post.PostCard
import com.sofamaniac.crabir.ui.thread.CommentNode
import com.sofamaniac.crabir.ui.thread.ThreadView
import com.sofamaniac.crabir.ui.thread.ThreadViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.max


/**
 * Composable function to display a list of posts from a subreddit.
 *
 * @param viewModel The current state of the SubredditViewer, including subreddit, sort order, and timeframe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFeedViewer(
    viewModel: FeedViewModelInterface,
    modifier: Modifier = Modifier
) {

    val posts = viewModel.data.collectAsLazyPagingItems()
    val listState = viewModel.listState

    LaunchedEffect(posts.loadState.refresh) {
        if (posts.loadState.refresh is LoadState.NotLoading && viewModel.needScrollToTop) {
            Log.d("PostFeedViewer", "Refreshing list")
            listState.scrollToItem(0)
        }
    }

    var mostVisibleItemIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow {
            if (!listState.isScrollInProgress) {
                listState.layoutInfo.visibleItemsInfo
                    .maxByOrNull { item ->
                        item.size
                        val itemTop = maxOf(item.offset.y, 0)
                        val itemBottom =
                            minOf(
                                item.offset.y + item.size.height,
                                listState.layoutInfo.viewportEndOffset
                            )
                        (itemBottom - itemTop).toFloat() / max(item.size.height, 1).toFloat()
                    }?.index ?: 0
            } else {
                null
            }
        }
            .filterNotNull()
            .collect { index ->
                mostVisibleItemIndex = index
            }
    }

    val fullscreenManager = LocalFullscreenHandler.current!!
    val viewSettings = rememberViewSettings()

    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(viewSettings.defaultColumns),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            //verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
        ) {
            items(count = posts.itemCount, key = posts.itemKey { p -> p.id }) { index ->
                val post = posts[index]!!
                val threadView = @Composable { post: PostData ->
                    HorizontalSwipeToDismiss {
                        ThreadView(
                            permalink = post.permalink,
                            dismiss = {
                                fullscreenManager.pop()
                            })
                    }
                }
                when (post) {
                    is PostData -> {
                        val onClick = { post: PostData ->
                            viewModel.visitPost(post)
                            fullscreenManager.push { threadView(post) }
                        }
                        val canStartVideo =
                            viewSettings.defaultColumns == 1 && index == mostVisibleItemIndex
                        val wasRead = viewModel.isPostRead(post)
                        when (viewSettings.defaultView) {
                            Views.Card -> PostCard(
                                post,
                                onClick = onClick,
                                canStartVideo = canStartVideo,
                                read = wasRead,
                            )

                            Views.Compact -> CompactView(
                                post,
                                onClick = onClick,
                                canStartVideo = canStartVideo,
                                read = wasRead,
                            )

                            else ->
                                PostCard(
                                    post,
                                    onClick = { post ->
                                        fullscreenManager.push { threadView(post) }
                                    },
                                    canStartVideo = canStartVideo,
                                )
                        }
                    }

                    is CommentData -> {
                        CommentNode(
                            comment = post,
                            viewModel = hiltViewModel<ThreadViewModel, ThreadViewModel.Factory> { factory ->
                                factory.create(post.permalink)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    state: PostFeedViewModel,
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    val scope = rememberCoroutineScope()
    val params = state.params.collectAsState()
    val theme = LocalTheme.current
    val drawerState = LocalDrawerState.current

    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = theme.toolbarBackground,
            scrolledContainerColor = theme.toolbarBackground,
            titleContentColor = theme.toolbarText,
        ),
        title = {
            Column {
                Text(title)
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(params.value.sort.toString(), style = MaterialTheme.typography.labelSmall)
                    if (params.value.timeframe != null) {
                        Text(".", style = MaterialTheme.typography.labelSmall)
                        Text(
                            params.value.timeframe.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) { Icon(Icons.Default.Menu, "Open Drawer") }
        },
        actions = {
            // Sort Dropdown
            var showMenu by remember { mutableStateOf(false) }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.MoreVert, "Options")
            }

            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(onClick = { }, text = { Text("Settings") })
                DropdownMenuItem(onClick = { }, text = { Text("Info") })
                DropdownMenuItem(onClick = { state.refresh() }, text = { Text("Refresh") })
            }
            SortMenu<Sort> { sort, timeframe ->
                state.updateSort(sort, timeframe)
            }
        }
    )
}