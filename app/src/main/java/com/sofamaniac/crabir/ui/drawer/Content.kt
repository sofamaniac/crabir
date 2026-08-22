/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:33 PM
 *
 */

package com.sofamaniac.crabir.ui.drawer

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalLateralMenuSettings
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.PreviewLocalComposition
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.dto.subreddit.dummySubredditData
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.MultiRoute
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.ui.drawer.buttons.BlurTile
import com.sofamaniac.crabir.ui.drawer.buttons.DarkModeTile
import com.sofamaniac.crabir.ui.drawer.buttons.FeedButtons
import com.sofamaniac.crabir.ui.drawer.buttons.MultiTile
import com.sofamaniac.crabir.ui.drawer.buttons.NSFWTile
import com.sofamaniac.crabir.ui.drawer.buttons.SettingsTile
import com.sofamaniac.crabir.ui.drawer.buttons.SubredditTile
import com.sofamaniac.crabir.ui.drawer.buttons.feeds
import com.sofamaniac.crabir.ui.drawer.buttons.goToMenu
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    viewModel: DrawerViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val sortedSubscriptions by viewModel.sortedSubscriptions.collectAsState()
    val multis by viewModel.multis.collectAsState()
    val selectingAccount by viewModel.selectingAccount.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val loginState by viewModel.loginState.collectAsState()

    viewModel.initialize()

    val snackbarHostState = LocalSnackBarHost.current
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Error) {
            snackbarHostState?.showSnackbar(message = "Something went wrong: ${(loginState as LoginState.Error).message}")
        }
    }

    DrawerContent(
        drawerState = drawerState,
        sortedSubscriptions = sortedSubscriptions,
        multis = multis,
        onFeedClick = { feed ->
            coroutineScope.launch {
                drawerState.close()
                navController?.navigate(feed.route)
            }
        },
        onMultiClick = { multi ->
            coroutineScope.launch {
                drawerState.close()
                viewModel.visitCommunity(multi.data)
                navController?.navigate(
                    MultiRoute(
                        multi.data.displayNamePrefixed
                    )
                )
            }
        },
        onSubredditClick = { subreddit ->
            coroutineScope.launch {
                drawerState.close()
                viewModel.visitCommunity(
                    SubredditDTOMapper.map(
                        subreddit.data
                    )
                )
                navController?.navigate(
                    SubredditRoute(
                        subreddit.data.displayNamePrefixed
                    )
                )
            }
        },
        accountSelector = {
            AccountSelector(
                viewModel,
                expanded = selectingAccount
            ) { id ->
                coroutineScope.launch {
                    drawerState.close()
                    viewModel.toggleSelectAccount()
                    viewModel.setActiveAccount(id)
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun DrawerContent(
    sortedSubscriptions: List<Thing.Subreddit>,
    drawerState: DrawerState,
    multis: List<Thing.Multi>,
    onFeedClick: (FeedButtons) -> Unit,
    onMultiClick: (Thing.Multi) -> Unit,
    onSubredditClick: (Thing.Subreddit) -> Unit,
    accountSelector: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d("DrawerContentStateless", "recompose ${drawerState.isClosed}")
    var expandGoTo by remember { mutableStateOf(false) }
    val settings = LocalLateralMenuSettings.current
    val navController = LocalNavController.current
    val sortedSubscriptions =
        sortedSubscriptions.filter { !settings.showFavOnly || it.data.userHasFavorited }
    ModalDrawerSheet(drawerState = drawerState) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(0.75f)
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            item {
                accountSelector()
            }
            item { HorizontalDivider() }
            feeds(settings.items) { route ->
                navController?.navigate(route)
            }
            item { HorizontalDivider() }
            goToMenu(expandGoTo, onClick = { expandGoTo = !expandGoTo })
            if (settings.items.goToCommunity) {
                item {
                    NavigationDrawerItem(
                        label = { Text("Go to community") },
                        selected = false,
                        icon = {
                            Icon(
                                Icons.Default.GroupWork,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            SearchRoute()
                        }
                    )
                }
            }
            if (settings.items.goToUser) {
                item {
                    NavigationDrawerItem(
                        label = { Text("Go to user") },
                        selected = false,
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            navController?.navigate(SearchRoute(initialTab = 2))
                        }
                    )
                }
            }
            if (settings.items.darkMode) {
                item {
                    DarkModeTile()
                }
            }
            if (settings.items.showNSFW) {
                item {
                    NSFWTile()
                }
            }
            if (settings.items.blurNSFW) {
                item { BlurTile() }
            }
            item { SettingsTile(drawerState) }
            item { HorizontalDivider() }
            items(multis) { multi ->
                MultiTile(multi, settings.showIcons) {
                    onMultiClick(multi)
                }
            }
            items(sortedSubscriptions) { subreddit ->
                SubredditTile(subreddit, settings.showIcons)
                {
                    onSubredditClick(subreddit)
                }
            }
        }
    }
}

@Preview
@Composable
private fun DrawerContentPreview() {
    PreviewLocalComposition {
        DrawerContent(
            drawerState = rememberDrawerState(DrawerValue.Closed),
            sortedSubscriptions = listOf(
                Thing.Subreddit(
                    dummySubredditData().copy(
                        displayName = "Kotlin",
                        displayNamePrefixed = "r/Kotlin"
                    )
                ),
                Thing.Subreddit(
                    dummySubredditData().copy(
                        displayName = "AndroidDev",
                        displayNamePrefixed = "r/AndroidDev"
                    )
                )
            ),
            multis = emptyList(),
            onFeedClick = {},
            onMultiClick = {},
            onSubredditClick = {},
            accountSelector = {
                AccountTile(
                    account = RedditAccount.anonymous(),
                    onClick = {},
                    iconModifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clip(CircleShape),
                    badge = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                )
            }
        )
    }
}

