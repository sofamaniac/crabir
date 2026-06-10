package com.sofamaniac.crabir.ui.postEditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.ui.search.CommunitySearchViewModel
import com.sofamaniac.crabir.ui.subredditList.Tile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommunitySearch(
    viewModel: CreatorViewModel,
    searchViewModel: CommunitySearchViewModel = hiltViewModel(),
    onDismiss: () -> Unit = {}
) {
    val searchState = rememberSearchBarState()
    val searchedCommunities = searchViewModel.items.collectAsLazyPagingItems()
    val subscriptions by searchViewModel.subscriptions.collectAsState()
    val listState = searchViewModel.listState
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Close search"
                            )
                        }
                    },
                    title = {
                        SearchBar(
                            searchState,
                            inputField = {
                                SearchBarDefaults.InputField(
                                    query = searchViewModel.query,
                                    onQueryChange = searchViewModel::onQueryUpdate,
                                    onSearch = {},
                                    expanded = false,
                                    placeholder = { Text("Search") },
                                    onExpandedChange = {}
                                )
                            }
                        )
                    })
            }
        ) { paddingValues ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(1),
                verticalItemSpacing = 8.dp,
                state = listState,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (searchedCommunities.itemCount > 0) {
                    items(
                        count = searchedCommunities.itemCount,
                        key = searchedCommunities.itemKey { p -> p.name }) { index ->
                        val subreddit = searchedCommunities[index]!!
                        Tile(subreddit, modifier = Modifier.clickable {
                            viewModel.setSubreddit(subreddit)
                            onDismiss()
                        })
                    }
                } else {
                    val subs = subscriptions.sortedBy { it.data.displayName.lowercase() }
                    items(
                        count = subs.size,
                        key = { subs[it].id }) { index ->
                        val subreddit = SubredditDTOMapper.map(subs[index].data)
                        Tile(subreddit, modifier = Modifier.clickable {
                            viewModel.setSubreddit(subreddit)
                            onDismiss()
                        })
                    }
                }
            }
        }

    }
}