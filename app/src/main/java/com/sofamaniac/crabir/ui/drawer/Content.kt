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
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.PreviewLocalComposition
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.dto.subreddit.dummySubredditData
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.MultiRoute
import com.sofamaniac.crabir.navigation.SettingsRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.settings.filters.filtersDataStore
import com.sofamaniac.crabir.settings.theme.ThemeMode
import com.sofamaniac.crabir.settings.theme.themeDataStore
import kotlinx.coroutines.flow.map
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

            items(FeedButtons.entries.toList()) { feed ->
                NavigationDrawerItem(
                    label = { Text(feed.name) }, icon = {
                        Icon(
                            feed.icon,
                            contentDescription = feed.name,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    selected = false,
                    onClick = {
                        onFeedClick(feed)
                    }
                )
            }
            item { HorizontalDivider() }
            item { BlurTile() }
            item { SettingsTile(drawerState) }
            item { HorizontalDivider() }
            items(multis) { multi ->
                MultiTile(multi) {
                    onMultiClick(multi)
                }
            }
            items(sortedSubscriptions) { subreddit ->
                SubredditTile(subreddit)
                {
                    onSubredditClick(subreddit)
                }
            }
        }
    }
}

@Composable
private fun SettingsTile(drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val context = LocalContext.current
    val themeDataStore = remember { context.themeDataStore }
    val themeMode by remember {
        themeDataStore.data.map { it.mode }
    }
        .collectAsState(initial = ThemeMode.System)
    NavigationDrawerItem(
        label = {
            Text("Settings")
        },
        badge = {
            if (themeMode == ThemeMode.Dark || themeMode == ThemeMode.Light) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        themeDataStore.updateData {
                            if (themeMode == ThemeMode.Dark) {
                                it.copy(mode = ThemeMode.Light)
                            } else {
                                it.copy(mode = ThemeMode.Dark)
                            }
                        }
                    }
                }) {
                    if (themeMode == ThemeMode.Dark) {
                        Icon(Icons.Default.LightMode, contentDescription = "Light mode")
                    } else {
                        Icon(Icons.Default.DarkMode, contentDescription = "Dark mode")
                    }
                }
            }
        },
        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
        selected = false,
        onClick = {
            coroutineScope.launch {
                drawerState.close()
                navController?.navigate(SettingsRoute)
            }
        }
    )
}

@Composable
private fun BlurTile() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val filtersDataStore = remember { context.filtersDataStore }
    val blur by remember {
        filtersDataStore.data.map { it.blurNSFW }
    }.collectAsState(initial = false)

    fun toggle() {
        coroutineScope.launch {
            filtersDataStore.updateData {
                it.copy(blurNSFW = !it.blurNSFW)
            }
        }
    }
    NavigationDrawerItem(
        selected = false,
        icon = { Icon(Icons.Default.BlurOn, contentDescription = "Blur NSFW") },
        label = {
            Text("Blur NSFW")
        },
        badge = {
            Switch(
                blur, onCheckedChange = {
                    toggle()
                }
            )
        },
        onClick = { toggle() }
    )
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

