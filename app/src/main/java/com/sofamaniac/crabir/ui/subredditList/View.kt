/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.crabir.ui.subredditList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.BackButton
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior?, query: String, onSearch: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val navController = LocalNavController.current
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            BackButton { navController?.popBackStack() }
        },
        title = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = onSearch,
                        onSearch = onSearch,
                        placeholder = { Text(stringResource(R.string.go_to_placeholder)) },
                        expanded = expanded,
                        onExpandedChange = {},
                    )
                },
                expanded = expanded,
                onExpandedChange = {},
                content = {}
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditListViewer(
    viewModel: SubscriptionViewModel = koinViewModel(),
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val subscriptions by viewModel.filteredSubs.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val filter by viewModel.filter.collectAsState()

    Scaffold(topBar = { TopBar(scrollBehavior, filter, viewModel::setFilter) }) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                //scope.launch { viewModel.refresh() }
                isRefreshing = false
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    //.background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                state = listState
            ) {
                items(count = subscriptions.size) { index ->
                    val subreddit = SubredditDTOMapper.map(subscriptions[index].data)
                    Tile(subreddit)
                    HorizontalDivider()
                }
            }
        }
    }
}
