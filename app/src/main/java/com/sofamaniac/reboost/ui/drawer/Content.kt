/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:33 PM
 *
 */

package com.sofamaniac.reboost.ui.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.LicensesRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

@Composable
fun DrawerContent(
    viewModel: DrawerViewModel,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current!!
    val subscriptions = viewModel.subscriptions.collectAsState(initial = emptyList())
    val sortedSubscriptions = subscriptions.value?.sortedBy { it.data.display_name.lowercase() }
    val selectingAccount by viewModel.selectingAccount.collectAsState()
    val rotation =
        animateFloatAsState(targetValue = if (selectingAccount) 180f else 0f, label = "rotation")
    val coroutineScope = rememberCoroutineScope()
    ModalDrawerSheet {
        Column(
            modifier = modifier
                .fillMaxWidth(0.75f)
                .navigationBarsPadding()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            val account by viewModel.activeAccount.collectAsState(initial = RedditAccount.anonymous())
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
                AccountSelector(viewModel) {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    viewModel.toggleSelectAccount()
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
                    navController.navigate(feed.route)
                    coroutineScope.launch {
                        drawerState.close()
                    }
                })
            }

            Spacer(modifier = Modifier.padding(16.dp))
            IconButton(onClick = {
                navController.navigate(LicensesRoute)
            }) {
                Icon(Icons.Default.Info, contentDescription = "About")
            }
            HorizontalDivider()
            for (multi in viewModel.multis.collectAsState(initial = emptyList()).value) {
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
                    onClick = {
                        navController.navigate(
                            com.sofamaniac.reboost.MultiRoute(
                                multi.data.displayName,
                                multi.data.permalink,
                            )
                        )
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    })
            }
            for (subreddit in sortedSubscriptions ?: emptyList()) {
                NavigationDrawerItem(
                    label = { Text(subreddit.data.display_name) },
                    selected = false,
                    icon = {
                        SubredditIcon(
                            subreddit.data.display_name,
                            subreddit.data.icon,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    },
                    onClick = {
                        navController.navigate(
                            com.sofamaniac.reboost.SubredditRoute(
                                subreddit.data.display_name
                            )
                        )
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    })
            }
        }
    }
}

