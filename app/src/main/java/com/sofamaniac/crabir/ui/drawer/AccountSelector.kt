package com.sofamaniac.crabir.ui.drawer

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.ThemedDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelector(
    viewModel: DrawerViewModel,
    expanded: Boolean,
    onAccountSelection: (Int) -> Unit,
) {
    val iconModifier = Modifier
        .size(48.dp)
        .padding(4.dp)
        .clip(CircleShape)
    var showWarningDialog by remember { mutableStateOf(false) }
    val authLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("AccountSelector", "Result: $result")
        viewModel.handleAuthResult(result.data)
    }
    val rotation =
        animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "rotation")
    val activeAccount by viewModel.activeAccount.collectAsState(RedditAccount.anonymous())
    val otherAccounts by viewModel.otherAccounts.collectAsState(emptyList())
    Column {
        AccountTile(
            activeAccount,
            onClick = viewModel::toggleSelectAccount,
            iconModifier = iconModifier,
            badge = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Select account",
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(rotation.value)
                )
            }
        )
        AnimatedVisibility(expanded) {
            Column {
                for (account in otherAccounts) {
                    AccountTile(
                        account,
                        onClick = {
                            onAccountSelection(account.id)
                        },
                        iconModifier = iconModifier
                    )
                }
                if (!activeAccount.isAnonymous()) {
                    AccountTile(
                        RedditAccount.anonymous(),
                        onClick = {
                            onAccountSelection(RedditAccount.anonymous().id)
                        },
                        iconModifier = iconModifier
                    )
                }
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = iconModifier
                        )
                    },
                    label = { Text(stringResource(R.string.add_account)) },
                    selected = false,
                    onClick = {
                        showWarningDialog = true
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            modifier = iconModifier
                        )
                    },
                    label = { Text(stringResource(R.string.logout)) },
                    selected = false,
                    onClick = {
                        viewModel.logout()
                    }
                )
            }
        }
    }
    if (showWarningDialog) {
        ThemedDialog(
            onDismissRequest = { showWarningDialog = false },
            cancel = {
                TextButton(onClick = { showWarningDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirm = {
                TextButton(onClick = {
                    showWarningDialog = false
                    val authIntent = viewModel.createAuthIntent()
                    authLauncher.launch(authIntent)
                }) {
                    Text(stringResource(R.string._continue))
                }
            }) {
            ListItem(
                content = { Text(stringResource(R.string.login_dialog_warning_title)) }
            )
            ListItem(
                content = {
                    Text(stringResource(R.string.login_warning))
                },
                modifier = Modifier.padding(16.dp)
            )
            Row {
                Spacer(modifier = Modifier.weight(1f))
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
    badge: @Composable (() -> Unit)? = null,
) {
    val image = if (account.info == null) {
        @Composable {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = iconModifier
            )
        }
    } else {
        @Composable {
            AsyncImage(
                model = account.info.iconImg,
                contentDescription = null,
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
