package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.ui.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: ThreadViewModel,
    scrollBehavior: TopAppBarScrollBehavior?,
    dismiss: () -> Unit,
) {
    val sort: Sort? by viewModel.sort.collectAsState()
    val theme = LocalTheme.current
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = theme.toolbarBackground,
            scrolledContainerColor = theme.toolbarBackground,
            titleContentColor = theme.toolbarText,
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            BackButton { dismiss() }
        },
        title = {
            Column {
                Text("Comments", style = MaterialTheme.typography.titleMedium)
                Text("${sort ?: Sort.Best}", style = MaterialTheme.typography.labelSmall)
            }
        },
        actions = {
            Icon(Icons.Default.Search, "Search comments")
            SortMenu(viewModel)
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, "More Options")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(text = { Text("refresh") }, onClick = { viewModel.refresh() })
                }
            }
        }
    )
}