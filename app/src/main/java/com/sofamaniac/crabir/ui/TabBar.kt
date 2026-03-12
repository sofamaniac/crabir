package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.sofamaniac.crabir.HomeRoute
import com.sofamaniac.crabir.InboxRoute
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.ProfileRoute
import com.sofamaniac.crabir.SearchRoute
import com.sofamaniac.crabir.SubscriptionsRoute
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabBarViewModel @Inject constructor(
    val accountsRepository: AccountsRepository
) : ViewModel() {
    val currentUser = accountsRepository.activeAccount
}


@Composable
fun TabBar(
    selected: Int,
    modifier: Modifier = Modifier,
    onTabReselect: (() -> Unit)? = null,
    viewModel: TabBarViewModel = hiltViewModel()
) {
    val user by viewModel.currentUser.collectAsState(initial = RedditAccount.anonymous())
    val tabs = listOf(
        Pair(Icons.Filled.Home, HomeRoute),
        Pair(Icons.Default.Search, SearchRoute()),
        Pair(Icons.AutoMirrored.Outlined.List, SubscriptionsRoute),
        Pair(Icons.Default.Email, InboxRoute),
        Pair(Icons.Filled.Person, ProfileRoute(user.username))
    )
    val navController = LocalNavController.current!!
    PrimaryTabRow(
        selectedTabIndex = selected,
        modifier = modifier.navigationBarsPadding(),
        indicator = {}) {
        for ((index, tab) in tabs.withIndex()) {
            Tab(
                selected = selected == index,
                unselectedContentColor = Color.Gray,
                onClick = {
                    if (onTabReselect != null && selected == index) {
                        return@Tab onTabReselect()
                    }
                    navController.navigate(tab.second) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = index == 0
                        }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(tab.first, contentDescription = tab.second.title) },
            )
        }
    }
}

