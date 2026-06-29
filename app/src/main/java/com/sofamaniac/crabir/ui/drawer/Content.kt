/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:33 PM
 *
 */

package com.sofamaniac.crabir.ui.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.MultiRoute
import com.sofamaniac.crabir.navigation.SettingsRoute
import com.sofamaniac.crabir.navigation.SubredditRoute
import com.sofamaniac.crabir.settings.filters.filtersDataStore
import com.sofamaniac.crabir.settings.theme.ThemeMode
import com.sofamaniac.crabir.settings.theme.themeDataStore
import com.sofamaniac.crabir.ui.subreddit.SubredditIcon
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    viewModel: DrawerViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val subscriptions = viewModel.subscriptions.collectAsState(initial = emptyList())
    val sortedSubscriptions = subscriptions.value?.sortedWith { subreddit1, subreddit2 ->
        if (subreddit1 == subreddit2) {
            return@sortedWith 0
        } else if (subreddit1.data.userHasFavorited != subreddit2.data.userHasFavorited) {
            // favorited subs should be first
            return@sortedWith if (subreddit1.data.userHasFavorited) -1 else 1
        } else {
            return@sortedWith subreddit1.data.displayName.lowercase()
                .compareTo(subreddit2.data.displayName.lowercase())
        }
    }
    //{ it.data.displayName.lowercase() }
    val selectingAccount by viewModel.selectingAccount.collectAsState()
    val rotation =
        animateFloatAsState(targetValue = if (selectingAccount) 180f else 0f, label = "rotation")
    val coroutineScope = rememberCoroutineScope()
    val drawerState = LocalDrawerState.current
    val account by viewModel.activeAccount.collectAsState(initial = RedditAccount.anonymous())
    val loginState by viewModel.loginState.collectAsState()
    LaunchedEffect(Unit) {
        // Reset when account changes
        viewModel.activeAccount
            .drop(1) // skip initial emission
            .collect {
                navController?.navigate(HomeRoute) {
                    popUpTo(0) { inclusive = true }
                    //launchSingleTop = true
                }
            }
    }

    val snackbarHostState = LocalSnackBarHost.current
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Error) {
            snackbarHostState?.showSnackbar(message = "Something went wrong: ${(loginState as LoginState.Error).message}")
        }
    }

    ModalDrawerSheet(drawerState = drawerState) {
        Column(
            modifier = modifier
                .fillMaxWidth(0.75f)
                .navigationBarsPadding()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            AccountTile(
                account,
                onClick = viewModel::toggleSelectAccount,
                iconModifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                badge = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Select account",
                        modifier = Modifier
                            .size(32.dp)
                            .rotate(rotation.value)
                    )
                })
            AnimatedVisibility(selectingAccount) {
                AccountSelector(viewModel) { id ->
                    coroutineScope.launch {
                        drawerState.close()
                        navController?.popBackStack(route = HomeRoute, inclusive = false)
                        viewModel.toggleSelectAccount()
                        viewModel.setActiveAccount(id)
                    }
                }
            }
            HorizontalDivider()

            for (feed in FeedButtons.entries) {
                NavigationDrawerItem(label = { Text(feed.name) }, icon = {
                    Icon(
                        feed.icon,
                        contentDescription = feed.name,
                        modifier = Modifier.size(32.dp)
                    )
                }, selected = false, onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                        navController?.navigate(feed.route)
                    }
                })
            }
            HorizontalDivider()
            BlurTile()
            SettingsTile()
            HorizontalDivider()
            for (multi in viewModel.multis.collectAsState(initial = emptyList()).value) {
                MultiTile(multi) {
                    coroutineScope.launch {
                        drawerState.close()
                        viewModel.visitCommunity(multi.data)
                        navController?.navigate(
                            MultiRoute(
                                multi.data.displayNamePrefixed
                            )
                        )
                    }
                }
            }
            for (subreddit in sortedSubscriptions ?: emptyList()) {
                SubredditTile(subreddit)
                {
                    coroutineScope.launch {
                        drawerState.close()
                        viewModel.visitCommunity(
                            SubredditDTOMapper.map(
                                subreddit.data
                            )
                        )
                        navController?.navigate(
                            SubredditRoute(
                                subreddit.data.displayName
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun MultiTile(multi: Thing.Multi, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(multi.data.displayName) },
        selected = false,
        icon = {
            AsyncImage(
                multi.data.iconUrl,
                "${multi.data.displayName} icon",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        },
        onClick = onClick
    )
}

@Composable
internal fun SubredditTile(
    subreddit: Thing.Subreddit,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    NavigationDrawerItem(
        label = { Text(subreddit.data.displayName) },
        selected = false,
        icon = {
            SubredditIcon(
                subreddit.data.displayName,
                subreddit.data.icon,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        },
        badge = if (!subreddit.data.userHasFavorited) null else {
            {
                Icon(Icons.Filled.Star, tint = theme.saved, contentDescription = null)
            }
        },
        onClick = onClick
    )
}

@Composable
fun SettingsTile() {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = LocalDrawerState.current
    val navController = LocalNavController.current!!
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
                navController.navigate(SettingsRoute)
            }
        }
    )
}

@Composable
fun BlurTile() {
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

