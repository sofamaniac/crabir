/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.crabir.ui.subredditList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.navigation.LocalNavController
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior?) {
    var expanded by remember { mutableStateOf(true) }
    var currentSearch by remember { mutableStateOf("") }
    val navController = LocalNavController.current
    TopAppBar(scrollBehavior = scrollBehavior, title = {
        Row {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = currentSearch,
                        onQueryChange = { currentSearch = it },
                        onSearch = { },
                        placeholder = { Text(stringResource(R.string.go_to_placeholder)) },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },

                        )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) { }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditListViewer(
    viewModel: SubscriptionViewModel = koinViewModel(),
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val subscriptions by viewModel.subscriptions.collectAsState()

    val sortedSubs =
        subscriptions?.sortedBy { it.data.displayName.lowercase() } ?: emptyList()
    TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold { innerPadding ->
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = listState
            ) {
                items(count = sortedSubs.size) { index ->
                    val subreddit = SubredditDTOMapper.map(sortedSubs[index].data)
                    Tile(subreddit)
                }
            }
        }
    }
}
