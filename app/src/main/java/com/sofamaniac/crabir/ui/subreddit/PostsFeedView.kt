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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.settings.views.Views
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.SortMenu
import com.sofamaniac.crabir.ui.post.CompactView
import com.sofamaniac.crabir.ui.post.PostCard
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.max

typealias ThingView<T> = @Composable (thing: T, isMostVisible: Boolean) -> Unit

object PostFeedViewerDefaults {
    fun hiddenFilter(votableData: VotableData?): Boolean {
        return when (votableData) {
            is PostData -> {
                !votableData.relationship.hidden
            }

            else -> true
        }
    }
}

/**
 * Composable function to display a list of posts from a subreddit.
 *
 * @param viewModel The current state of the SubredditViewer, including subreddit, sort order, and timeframe.
 * @param filter Renders only the elements for which filter returns true
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : VotableData> PostFeedViewer(
    viewModel: FeedViewModelInterface<T>,
    modifier: Modifier = Modifier,
    // If set to {}, breaks pull to refresh
    feedInfo: (@Composable () -> Unit)? = null,
    filter: (T?) -> Boolean = PostFeedViewerDefaults::hiddenFilter,
    itemView: @Composable (thing: T, isMosVisible: Boolean) -> Unit
) {

    val posts = viewModel.data.collectAsLazyPagingItems()
    val listState = viewModel.listState


    LaunchedEffect(posts.loadState.refresh) {
        if (posts.loadState.refresh is LoadState.NotLoading && viewModel.needScrollToTop) {
            Log.d("PostFeedViewer", "Refreshing list")
            listState.scrollToItem(0)
            viewModel.needScrollToTop = false
        }
    }

    var mostVisibleItemIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow {
            if (!listState.isScrollInProgress) {
                listState.layoutInfo.visibleItemsInfo
                    .maxByOrNull { item ->
                        val itemTop = maxOf(item.offset.y, 0)
                        val itemBottom =
                            minOf(
                                item.offset.y + item.size.height,
                                listState.layoutInfo.viewportEndOffset
                            )
                        val visibleHeight = (itemBottom - itemTop).toFloat()
                        visibleHeight / max(item.size.height, 1).toFloat()
                    }?.index ?: 0
            } else {
                null
            }
        }
            .filterNotNull()
            .collect { index ->
                mostVisibleItemIndex = if (feedInfo != null) {
                    index - 1
                } else {
                    index
                }
            }
    }

    //val fullscreenManager = LocalFullscreenHandler.current!!
    val viewSettings = rememberViewSettings()
    val navController = LocalNavController.current!!
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        state = state,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize(),
        indicator = {
            val refreshing = posts.loadState.refresh == LoadState.Loading
            Box(modifier = Modifier.fillMaxWidth()) {
                if (refreshing) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else {
                    PullToRefreshDefaults.Indicator(
                        state,
                        isRefreshing = false,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    ) {
        // Do not render the lazylist if there are no items
        // This is necessary because when going back from another page,
        // posts is at first empty and causes the list to lose its scroll state.
        if (posts.itemCount == 0) return@PullToRefreshBox
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(viewSettings.defaultColumns),
            verticalItemSpacing = 2.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            //verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
        ) {
            if (feedInfo != null) {
                item(
                    key = "info",
                    span = StaggeredGridItemSpan.FullLine
                ) { feedInfo() }
            }
            items(count = posts.itemCount, key = posts.itemKey { p -> p.id }) { index ->
                val isMostVisible = index == mostVisibleItemIndex
                val post = posts[index]
                if (post != null && filter(post)) {
                    itemView(post, isMostVisible)
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    params: FeedParams,
    updateSort: (Sort, Timeframe?) -> Unit,
    refresh: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    infoButton: (@Composable () -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
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
                    val sortString = stringResource(params.sort.representation)
                    val timeString = params.timeframe?.let { stringResource(it.representation) }
                    val fullString = if (params.timeframe != null) {
                        "$sortString • $timeString"
                    } else {
                        sortString
                    }
                    Text(fullString, style = MaterialTheme.typography.labelSmall)
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
                // TODO
                DropdownMenuItem(onClick = { }, text = { Text("Settings") })
                DropdownMenuItem(onClick = { }, text = { Text("Info") })
                DropdownMenuItem(onClick = { refresh() }, text = { Text("Refresh") })
            }
            infoButton?.invoke()
            SortMenu<Sort> { sort, timeframe ->
                updateSort(sort, timeframe)
            }
        }
    )
}

@Composable
fun DefaultPostView(
    thing: PostData,
    viewModel: FeedViewModelInterface<PostData>,
    isMostVisible: Boolean
) {

    val viewSettings = rememberViewSettings()
    val navController = LocalNavController.current!!
    val onClick = { post: PostData ->
        viewModel.visitPost(post)
        navController.navigate(PostRoute(post.permalink, null))
    }
    val canStartVideo =
        viewSettings.defaultColumns == 1 && isMostVisible
    val wasRead = viewModel.isPostRead(thing)
    when (viewSettings.defaultView) {
        Views.Card -> PostCard(
            thing,
            onClick = onClick,
            canStartVideo = canStartVideo,
            read = wasRead,
        )

        Views.Compact -> CompactView(
            thing,
            onClick = onClick,
            canStartVideo = canStartVideo,
            read = wasRead,
        )

        else ->
            PostCard(
                thing,
                onClick = { post ->
                    navController.navigate(PostRoute(post.permalink, null))
                },
                canStartVideo = canStartVideo,
            )
    }
}