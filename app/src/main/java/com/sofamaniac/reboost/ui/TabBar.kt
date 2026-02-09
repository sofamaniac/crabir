package com.sofamaniac.reboost.ui

import android.util.Log
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
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.HomeRoute
import com.sofamaniac.reboost.InboxRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.SearchRoute
import com.sofamaniac.reboost.SubscriptionsRoute

@Composable
fun TabBar(
    selected: State<Int>,
    modifier: Modifier = Modifier,
    onTabReselect: (() -> Unit)? = null
) {
    val tabs = listOf(
        Pair(Icons.Filled.Home, HomeRoute),
        Pair(Icons.Default.Search, SearchRoute),
        Pair(Icons.AutoMirrored.Outlined.List, SubscriptionsRoute),
        Pair(Icons.Default.Email, InboxRoute),
        Pair(Icons.Filled.Person, ProfileRoute("me"))
    )
    val navController = LocalNavController.current!!
    PrimaryTabRow(selectedTabIndex = selected.value, modifier = modifier.navigationBarsPadding()) {
        for ((index, tab) in tabs.withIndex()) {
            Tab(
                selected = selected.value == index,
                onClick = {
                    Log.d("TabBar", "Clicked on tab ${tab.second.title}")
                    if (onTabReselect != null && selected.value == index)
                        return@Tab onTabReselect()
                    navController.navigate(tab.second) {
                        Log.d("TabBar", "Navigating to ${tab.second.title}")
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

