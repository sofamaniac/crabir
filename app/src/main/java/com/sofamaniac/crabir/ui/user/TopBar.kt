package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.domain.repository.profile.ProfileSort
import com.sofamaniac.crabir.ui.SortMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    user: String,
    modifier: Modifier = Modifier,
    viewModel: ProfileFeedViewModel?
) {
    val scope = rememberCoroutineScope()
    val drawerState = LocalDrawerState.current
    TopAppBar(scrollBehavior = scrollBehavior, title = {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(user)
        }

    }, navigationIcon = {
        IconButton(onClick = { scope.launch { drawerState.open() } }) {
            Icon(
                Icons.Default.Menu, "Open Drawer"
            )
        }
    }, actions = {
        when (viewModel) {
            is SortProfileTab -> {
                SortMenu<ProfileSort> { sort, timeframe ->
                    viewModel.updateSort(sort, timeframe)
                }
            }

            else -> {
                Log.d("TopBar", "No sort menu")
            }
        }
    })
}