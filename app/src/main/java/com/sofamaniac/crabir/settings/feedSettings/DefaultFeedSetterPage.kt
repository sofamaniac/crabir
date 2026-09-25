package com.sofamaniac.crabir.settings.feedSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.HOME
import com.sofamaniac.crabir.ui.components.CloseButton
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.MultiIcon
import com.sofamaniac.crabir.ui.search.CommunitySearchViewModel
import com.sofamaniac.crabir.ui.subredditList.Tile
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultFeedSetterPage(
    searchViewModel: CommunitySearchViewModel = koinViewModel(),
    onDismiss: () -> Unit,
) {
    val searchState = rememberSearchBarState()
    val searchedCommunities = searchViewModel.items.collectAsLazyPagingItems()
    val subscriptions by searchViewModel.subscriptions.collectAsState()
    val multis by searchViewModel.multis.collectAsState()
    val listState = searchViewModel.listState
    val context = LocalContext.current
    val feedSettingsStore = context.feedSettingsStore
    val scope = rememberCoroutineScope()
    val select = { s: String ->
        scope.launch {
            feedSettingsStore.updateData {
                it.copy(defaultFeed = s)
            }
        }.invokeOnCompletion {
            onDismiss()
        }
    }
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        CloseButton { onDismiss() }
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
                                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
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
                            select(subreddit.displayNamePrefixed)
                        })
                    }
                } else {
                    item {
                        ListItem(
                            onClick = { select(HOME) },
                            leadingContent = {
                                Icon(
                                    Icons.Default.RssFeed,
                                    contentDescription = null
                                )
                            },
                            content = { Text(stringResource(R.string.home_feed)) }
                        )
                    }
                    val multis = multis.sortedBy { it.data.displayName.lowercase() }
                    items(count = multis.size, key = { multis[it].id }) { index ->
                        val multi = multis[index]
                        ListItem(
                            onClick = { select(multi.data.displayNamePrefixed) },
                            leadingContent = {
                                MultiIcon(multi.data)
                            },
                            content = { Text(multi.data.displayNamePrefixed) }
                        )
                    }
                    val subs = subscriptions.sortedBy { it.data.displayName.lowercase() }
                    items(
                        count = subs.size,
                        key = { subs[it].id }) { index ->
                        val subreddit = SubredditDTOMapper.map(subs[index].data)
                        Tile(subreddit, modifier = Modifier.clickable {
                            select(subreddit.displayNamePrefixed)
                        })
                    }
                }
            }
        }
    }
}
