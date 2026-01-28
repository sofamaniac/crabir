/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:33 PM
 *
 */

package com.sofamaniac.reboost.ui.drawer

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LicensesRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon
import java.util.Collections.emptyList

@Composable
fun DrawerContent(
    viewModel: DrawerViewModel,
    onAccountChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val authLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleAuthResult(result.data)
    }
    val navController = LocalNavController.current!!
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .navigationBarsPadding()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            val account by viewModel.activeAccount.collectAsState(initial = RedditAccount.anonymous())
            Text(account.username)
            for (account in viewModel.accountsList.collectAsState(initial = emptyList()).value) {
                Text(
                    account.username,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = {
                            viewModel.setActiveAccount(account.id)
                            onAccountChange()
                        })
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    val authIntent = viewModel.createAuthIntent()
                    authLauncher.launch(authIntent)
                }
            ) {
                Text("Login")
            }
            IconButton(onClick = {
                navController.navigate(LicensesRoute)
            }) {
                Icon(Icons.Default.Info, contentDescription = "About")
            }
            HorizontalDivider()
            for (subreddit in viewModel.subscriptions.collectAsState(initial = emptyList()).value!!) {
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
                    }
                )
            }
        }
    }
}
