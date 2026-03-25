package com.sofamaniac.crabir.ui.drawer

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.RedditAccount
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelector(viewModel: DrawerViewModel, onAccountSelection: () -> Unit) {
    val iconModifier = Modifier
        .size(32.dp)
        .padding(4.dp)
        .clip(CircleShape)
    val accounts by viewModel.accountsList.collectAsState(initial = Collections.emptyList())
    Log.d("AccountSelector", "accounts: $accounts")
    var showWarningDialog by remember { mutableStateOf(false) }
    val authLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("AccountSelector", "Result: $result")
        viewModel.handleAuthResult(result.data)
    }
    Column {
        for (account in accounts) {
            AccountTile(
                account,
                onClick = {
                    viewModel.setActiveAccount(account.id)
                    onAccountSelection()
                },
                iconModifier = iconModifier
            )
        }
        AccountTile(
            RedditAccount.anonymous(),
            onClick = {
                viewModel.setActiveAccount(-1)
                onAccountSelection()
            }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add account",
                    modifier = iconModifier
                )
            },
            label = { Text("Add account") },
            selected = false,
            onClick = {
                showWarningDialog = true
            }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    modifier = iconModifier
                )
            },
            label = { Text("Logout") },
            selected = false,
            onClick = {
                viewModel.logout()
            }
        )
    }
    val theme = LocalTheme.current
    if (showWarningDialog) {
        BasicAlertDialog(onDismissRequest = { showWarningDialog = false }) {
            Card {
                Column(modifier = Modifier.background(color = theme.cardBackground)) {
                    ListItem(
                        headlineContent = { Text("Warning") }
                    )
                    ListItem(
                        headlineContent = {
                            Text(
                                "You will not be able to log in on the next page because reddit is broken." +
                                        "Open reddit in your browser and log in before proceeding."
                            )
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                    Row() {
                        TextButton(onClick = { showWarningDialog = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = {
                            showWarningDialog = false
                            val authIntent = viewModel.createAuthIntent()
                            authLauncher.launch(authIntent)
                        }) {
                            Text("Continue")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountTile(
    account: RedditAccount,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    badge: @Composable (() -> Unit)? = null
) {
    val image = if (account.info == null) {
        @Composable {
            Icon(
                Icons.Default.Person,
                contentDescription = "Anonymous icon",
                modifier = iconModifier
            )
        }
    } else {
        @Composable {
            AsyncImage(
                model = account.info.iconImg,
                contentDescription = "${account.info.username} thumbnail",
                modifier = iconModifier
            )
        }
    }
    NavigationDrawerItem(
        label = { Text(account.info?.username ?: "Anonymous") },
        selected = false,
        onClick = onClick,
        icon = image,
        modifier = modifier,
        badge = badge
    )
}