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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.PreviewLocalComposition
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.settings.views.Views
import com.sofamaniac.crabir.ui.RefreshIndicator
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.post.CompactView
import com.sofamaniac.crabir.ui.post.DummyInteraction
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.PostViewModelInterface
import com.sofamaniac.crabir.ui.post.card.PostCard
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import kotlin.math.max

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
    filter: (T) -> Boolean = { true },
    // If set to {}, breaks pull to refresh
    feedInfo: (@Composable () -> Unit)? = null,
    itemView: @Composable (thing: T, isMostVisible: Boolean) -> Unit,
) {

    val posts = viewModel.data.collectAsLazyPagingItems()
    // Use another list state when they are no items.
    // See https://issuetracker.google.com/issues/177245496#comment24
    // This is necessary because when going back from another page,
    // posts is at first empty and causes the list to lose its scroll state.
    val listState = when (posts.itemCount) {
        0 -> rememberLazyStaggeredGridState()
        else -> viewModel.listState
    }

    LaunchedEffect(posts.loadState.refresh) {
        if (posts.loadState.refresh is LoadState.NotLoading && viewModel.needScrollToTop) {
            Log.d("PostFeedViewer", "Refreshing list")
            listState.scrollToItem(0)
            viewModel.needScrollToTop = false
        }
    }


    val mostVisibleItemKey by remember(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .maxByOrNull { item ->
                    // Compute the visible fraction for each item
                    val itemTop = maxOf(item.offset.y, 0)
                    val itemBottom =
                        minOf(
                            item.offset.y + item.size.height,
                            listState.layoutInfo.viewportEndOffset
                        )
                    val visibleHeight = (itemBottom - itemTop).toFloat()
                    visibleHeight / max(item.size.height, 1).toFloat()
                }?.key as? String?
        }.distinctUntilChanged()
    }.collectAsState(null)

    val viewSettings = LocalViewSettings.current
    val state = rememberPullToRefreshState()
    val theme = LocalTheme.current

    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        state = state,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier.fillMaxSize(),
        indicator = {
            val refreshing = posts.loadState.refresh == LoadState.Loading
            RefreshIndicator(refreshing, state)
        }
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(viewSettings.defaultColumns),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(theme.background)
                .fillMaxSize(),
            state = listState,
        ) {
            if (feedInfo != null) {
                item(
                    key = "info",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    feedInfo()
                }
            }
            items(count = posts.itemCount, key = posts.itemKey { p -> p.id }) { index ->
                val post = posts[index]
                if (post != null && filter(post)) {
                    val isMostVisible = mostVisibleItemKey == post.id
                    Column {
                        itemView(post, isMostVisible)
                        HorizontalDivider()
                    }
                }
            }
            item {
                val appendState = posts.loadState.append
                if (appendState is LoadState.NotLoading && appendState.endOfPaginationReached) {
                    ThemedCard {
                        Text("End of Feed reached")
                    }
                } else if (appendState is LoadState.Error) {
                    ThemedCard(modifier = Modifier.clickable { posts.retry() }) {
                        Text("Error while loading: ${appendState.error.localizedMessage}")
                    }
                }
            }
        }
    }
}

@Composable
fun PostView(
    thing: PostData,
    isMostVisible: Boolean,
    showHidden: Boolean,
    view: Views,
    viewModel: PostViewModelInterface,
    modifier: Modifier = Modifier,
) {
    when (view) {
        Views.Card -> PostCard(
            thing,
            modifier = modifier,
            isMostVisible = isMostVisible,
            showHidden = showHidden,
            viewModel = viewModel,
        )

        Views.Compact -> CompactView(
            thing,
            modifier = modifier,
            showHidden = showHidden,
            viewModel = viewModel,
        )
    }
}

@Composable
fun PostView(
    thing: PostData,
    isMostVisible: Boolean,
    showHidden: Boolean,
    modifier: Modifier = Modifier,
    view: Views? = null,
    viewModel: PostViewModelInterface = koinViewModel<LinkViewModel>(key = thing.id) {
        parametersOf(
            thing
        )
    },
) {
    val viewSettings = LocalViewSettings.current
    val view = if (!viewSettings.rememberView) viewSettings.defaultView else view
    PostView(
        thing,
        isMostVisible,
        showHidden,
        view ?: viewSettings.defaultView,
        viewModel = viewModel,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PostFeedPreview() {
    PreviewLocalComposition {
        KoinApplicationPreview(application = {
            modules(module {
                viewModel<DummyInteraction>() bind PostViewModelInterface::class
            })
        }) {
            PostFeedViewer(
                viewModel = FeedViewModelInterfacePreview
            ) { post, mostVisible ->
                PostView(
                    thing = post,
                    isMostVisible = mostVisible,
                    showHidden = false,
                    viewModel = koinViewModel<DummyInteraction>()
                )
            }
        }
    }
}
