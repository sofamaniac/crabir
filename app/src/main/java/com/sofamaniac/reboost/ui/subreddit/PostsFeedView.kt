/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:18 PM
 *
 */

package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.post.PostBody
import com.sofamaniac.reboost.ui.post.View
import com.sofamaniac.reboost.ui.thread.ThreadView
import kotlinx.coroutines.launch
import kotlin.math.max


/**
 * Composable function to display a list of posts from a subreddit.
 *
 * @param state The current state of the SubredditViewer, including subreddit, sort order, and timeframe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFeedViewer(
    state: PostFeedViewModel,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true
) {

    val posts = state.data.collectAsLazyPagingItems()
    val listState = state.listState

    val mostVisibleItemIndex by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .maxByOrNull { item ->
                    val size = item.size
                    val itemTop = maxOf(item.offset, 0)
                    val itemBottom =
                        minOf(item.offset + item.size, listState.layoutInfo.viewportEndOffset)
                    (itemBottom - itemTop).toFloat() / max(item.size, 1).toFloat()
                }?.index ?: 0
        }
    }
    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        onRefresh = {
            state.refresh()
        },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
        ) {
            items(count = posts.itemCount, key = posts.itemKey { p -> p.id.id }) { index ->
                val post = posts[index]!!
                val threadView = @Composable { post: PostData ->
                    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
                    SwipeToDismissBox(
                        state = swipeToDismissBoxState,
                        backgroundContent = {},
                        onDismiss = {
                            state.setFullscreenView(null)
                        }) {
                        ThreadView(
                            permalink = post.permalink,
                            dismiss = {
                                state.setFullscreenView(null)
                            })
                    }
                }
                View(
                    post,
                    showSubredditIcon = showSubredditIcon,
                    visitPost = { post ->
                        state.setFullscreenView { key(post.id.id) { threadView(post) } }
                    },
                ) {
                    PostBody(
                        post,
                        canPlayVideo = index == mostVisibleItemIndex,
                        goFullscreen = {
                            state.setFullscreenView(it)
                        }, dismiss = {
                            state.setFullscreenView(null)
                        })
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
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    val scope = rememberCoroutineScope()
    val params = state.params.collectAsState()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
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
            SortMenu(state)
        }
    )
}

@Composable
fun SortMenu(state: PostFeedViewModel) {
    var sortExpanded = remember { mutableStateOf(false) }
    var timeframeExpanded = remember { mutableStateOf(false) }
    var chosenSort by remember { mutableStateOf(state.params.value.sort) }
    Box {
        IconButton(onClick = { sortExpanded.value = true }) {
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
        }
        DropdownMenu(
            expanded = sortExpanded.value,
            onDismissRequest = { sortExpanded.value = false }) {
            Sort.entries.forEach { sort ->
                if (sort.isTimeframe()) {
                    DropdownMenuItem(text = { Text(sort.toString()) }, onClick = {
                        timeframeExpanded.value = true
                        chosenSort = sort
                    })
                } else {
                    DropdownMenuItem(text = { Text(sort.toString()) }, onClick = {
                        state.updateSort(sort)
                        sortExpanded.value = false
                    })
                }
            }
        }
        TimeframeMenu(state, sortExpanded, timeframeExpanded, chosenSort)
    }
}

@Composable
fun TimeframeMenu(
    state: PostFeedViewModel,
    sortExpanded: MutableState<Boolean>,
    expanded: MutableState<Boolean>,
    sort: Sort
) {
    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
        Timeframe.entries.forEach {
            DropdownMenuItem(text = { Text(it.toString()) }, onClick = {
                state.updateSort(sort, it)
                expanded.value = false
                sortExpanded.value = false
            })
        }
    }
}