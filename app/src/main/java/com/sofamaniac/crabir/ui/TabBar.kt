package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.InboxRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubscriptionsRoute

internal class TabRepresentation(val icon: ImageVector, val label: Int, val route: Route)


@Composable
fun TabBar(
    selected: Int,
    modifier: Modifier = Modifier,
    onTabReselect: (() -> Unit)? = null,
) {
    val user = LocalRedditAccount.current
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    fun onClick(selected: Int, index: Int, destination: Route) {
        if (onTabReselect != null && selected == index) {
            onTabReselect()
        } else {
            navController?.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = index == 0
                }
                launchSingleTop = true
            }
        }
    }
    PrimaryTabRow(
        selectedTabIndex = selected,
        modifier = modifier.navigationBarsPadding(),
        indicator = {}
    ) {
        Tab(
            selected = selected == 0,
            unselectedContentColor = Color.Gray,
            onClick = { onClick(selected, 0, HomeRoute) },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = stringResource(R.string.Home)
                )
            }
        )
        Tab(
            selected = selected == 1,
            unselectedContentColor = Color.Gray,
            onClick = { onClick(selected, 1, SearchRoute()) },
            icon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.Search)
                )
            }
        )
        Tab(
            selected = selected == 2,
            unselectedContentColor = Color.Gray,
            onClick = { onClick(selected, 2, SubscriptionsRoute) },
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(R.string.Subscriptions)
                )
            }
        )
        Tab(
            selected = selected == 3,
            unselectedContentColor = Color.Gray,
            onClick = { onClick(selected, 3, InboxRoute) },
            icon = {
                BadgedBox(badge = {
                    if ((user.info?.inboxCount ?: 0) > 0) {
                        Badge(containerColor = theme.highlight, contentColor = theme.contentColor) {
                            Text("${user.info?.inboxCount}")
                        }
                    }
                }) {
                    Icon(
                        Icons.Filled.Mail,
                        contentDescription = stringResource(R.string.Inbox)
                    )
                }
            }
        )
        Tab(
            selected = selected == 4,
            unselectedContentColor = Color.Gray,
            onClick = {
                if (!user.isAnonymous() && !user.isUninitialized()) {
                    onClick(selected, 4, ProfileRoute(user.info?.username ?: "Anonymous"))
                } else {
                    // TODO ask user to log in
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = stringResource(R.string.Profile)
                )
            }
        )
    }
}

