package com.sofamaniac.crabir.ui.search.community

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.ListItem
import com.sofamaniac.crabir.ui.search.CommunitySearchViewModel

@Composable
internal fun InnerTab(viewModel: CommunitySearchViewModel) {
    val things = viewModel.items.collectAsLazyPagingItems()
    val navController = LocalNavController.current
    val listState = viewModel.listState
    PullToRefreshBox(
        isRefreshing = things.loadState.refresh == LoadState.Loading,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = Modifier
            .fillMaxSize(),
        indicator = {
            if (things.loadState.refresh == LoadState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            verticalItemSpacing = 2.dp,
            state = listState, modifier = Modifier.fillMaxSize()
        ) {
            if (things.itemCount == 0) {
                fun onSuccess(sub: String) {
                    val url = sub.removePrefix("/")
                    navController?.navigate(SubredditRoute(url))
                }

                fun onError(e: Throwable) {
                    Log.e("SearchTab", "InnerTab: ", e)
                }
                item {
                    ListItem(onClick = {
                        viewModel.goToRandom(
                            false,
                            onSuccess = { onSuccess(it) },
                            onError = { onError(it) })
                    }) {
                        Text(stringResource(R.string.random_community))
                    }
                }
                item {
                    ListItem(onClick = {
                        viewModel.goToRandom(
                            true,
                            onSuccess = { onSuccess(it) },
                            onError = { onError(it) })
                    }) {
                        Text(stringResource(R.string.random_nsfw))
                    }
                }
            }
            items(
                count = things.itemCount,
                key = things.itemKey { p -> p.id }) { index ->
                val subreddit = things[index]!!
                SubredditItem(
                    subreddit,
                    viewModel::subscribe
                )
            }
        }
    }
}
