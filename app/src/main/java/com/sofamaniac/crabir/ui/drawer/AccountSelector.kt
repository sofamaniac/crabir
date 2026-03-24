package com.sofamaniac.crabir.ui.drawer

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.domain.model.RedditAccount
import java.util.Collections

@Composable
fun AccountSelector(viewModel: DrawerViewModel, onAccountSelection: () -> Unit) {
    val iconModifier = Modifier
        .size(32.dp)
        .padding(4.dp)
        .clip(CircleShape)
    val accounts by viewModel.accountsList.collectAsState(initial = Collections.emptyList())
    Log.d("AccountSelector", "accounts: $accounts")
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
        val authLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            viewModel.handleAuthResult(result.data)
        }
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
                Log.d("DrawerContent", "Launching auth intent")
                val authIntent = viewModel.createAuthIntent()
                authLauncher.launch(authIntent)
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