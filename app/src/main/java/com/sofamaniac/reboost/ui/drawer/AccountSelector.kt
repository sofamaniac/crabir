package com.sofamaniac.reboost.ui.drawer

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.HomeRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.domain.model.RedditAccount
import java.util.Collections

@Composable
fun AccountSelector(viewModel: DrawerViewModel, onAccountSelection: () -> Unit) {
    val iconModifier = Modifier
        .size(32.dp)
        .padding(4.dp)
        .clip(CircleShape)
    val navController = LocalNavController.current!!
    Column {
        for (account in viewModel.accountsList.collectAsState(initial = Collections.emptyList()).value) {
            AccountTile(
                account,
                onClick = {
                    viewModel.setActiveAccount(account.id)
                    navController.navigate(HomeRoute) {
                        restoreState = false
                    }
                    onAccountSelection()
                },
                iconModifier = iconModifier
            )
        }
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
            onClick = { viewModel.logout() }
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
    val image = if (account.thumbnailUrl.isBlank()) {
        @Composable {
            Icon(
                Icons.Default.Person,
                contentDescription = "${account.username} icon",
                modifier = iconModifier
            )
        }
    } else {
        @Composable {
            AsyncImage(
                model = account.thumbnailUrl,
                contentDescription = "${account.username} thumbnail",
                modifier = iconModifier
            )
        }
    }
    NavigationDrawerItem(
        label = { Text(account.username) },
        selected = false,
        onClick = onClick,
        icon = image,
        modifier = modifier,
        badge = badge
    )
}