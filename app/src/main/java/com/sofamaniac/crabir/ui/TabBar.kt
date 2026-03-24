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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.repository.rememberCurrentAccount
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.InboxRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubscriptionsRoute
import com.sofamaniac.crabir.ui.user.ProfileTabs

internal class TabRepresentation(val icon: ImageVector, val label: Int, val route: Route)


@Composable
fun TabBar(
    selected: Int,
    modifier: Modifier = Modifier,
    onTabReselect: (() -> Unit)? = null,
) {
    val user = rememberCurrentAccount()
    val tabs = listOf(
        TabRepresentation(Icons.Filled.Home, R.string.Home, HomeRoute),
        TabRepresentation(Icons.Default.Search, R.string.Search, SearchRoute()),
        TabRepresentation(
            Icons.AutoMirrored.Outlined.List, R.string.Subscriptions,
            SubscriptionsRoute
        ),
        TabRepresentation(Icons.Default.Email, R.string.Inbox, InboxRoute),
        TabRepresentation(
            Icons.Filled.Person,
            R.string.Profile,
            ProfileRoute(user.info?.username ?: "Anonymous", ProfileTabs.Overview)
        )
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
                    if (index == 4 && user.isAnonymous()) {
                        // TODO: ask user to log in
                        return@Tab
                    }
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = index == 0
                        }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(tab.icon, contentDescription = stringResource(tab.label)) },
            )
        }
    }
}

