package com.sofamaniac.reboost.ui.search

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.SearchRoute
import com.sofamaniac.reboost.ui.subreddit.FullFeedView

@Composable
fun SearchTab(
    searchQuery: SearchRoute,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    LaunchedEffect(searchQuery) {
        Log.d("SearchTab", "SearchTab: ${searchQuery.flair}")
        if (searchQuery.subreddit.isNotBlank()) viewModel.setSubreddit(searchQuery.subreddit)
        if (searchQuery.flair.isNotBlank()) viewModel.onQueryUpdate("flair:\"${searchQuery.flair}\"")
    }
    FullFeedView(
        topBar = { TopBar(viewModel) },
        bottomBar = {},
        modifier = modifier,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: SearchViewModel) {
    val navController = LocalNavController.current!!
    val state = rememberSearchBarState()
    TopAppBar(
        title = {
            SearchBar(
                state,
                inputField = {
                    SearchBarDefaults.InputField(
                        query = viewModel.query,
                        onQueryChange = viewModel::onQueryUpdate,
                        onSearch = {},
                        expanded = false,
                        placeholder = { Text("Search") },
                        onExpandedChange = {}
                    )
                })
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}