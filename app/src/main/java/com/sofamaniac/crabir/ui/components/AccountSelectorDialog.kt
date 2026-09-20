package com.sofamaniac.crabir.ui.components

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.AccountManager
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.ui.drawer.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

@Composable
fun AccountSelectorDialog(onDismiss: () -> Unit, viewModel: AccountViewModel = koinViewModel()) {
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
    val activeAccount by viewModel.activeAccount.collectAsState(RedditAccount.anonymous())
    val otherAccounts by viewModel.otherAccounts.collectAsState(emptyList())
    val onAccountSelection = { accountId: Int ->
        viewModel.setActiveAccount(accountId)
        onDismiss()
    }
    ThemedDialog(onDismissRequest = onDismiss) {
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
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = iconModifier
                    )
                },
                content = { Text(stringResource(R.string.add_account)) },
                selected = false,
                onClick = {
                    showWarningDialog = true
                }
            )
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

@KoinViewModel
class AccountViewModel(
    private val accountsRepository: AccountsRepository,
    private val redditApi: RedditAPIService,
    private val accountManager: AccountManager,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    val activeAccount = accountsRepository.activeAccount
    val otherAccounts = combine(
        accountsRepository.accounts,
        accountsRepository.activeAccount
    ) { accounts, active ->
        accounts.filterNot { it.id == active.id || it.isAnonymous() }
            .sortedByDescending { it.id }
    }

    fun setActiveAccount(accountId: Int) {
        viewModelScope.launch {
            if (activeAccount.first().id == accountId) return@launch
            accountsRepository.setActiveAccount(accountId)
            val account = activeAccount.first()
            Log.d(
                "LoginViewModel",
                "Setting active account to '${activeAccount.first().info?.username ?: "Anonymous"}'"
            )
            if (account.info?.username.isNullOrBlank() && !account.isAnonymous()) {
                fetchUserInfo()
            }
        }
    }

    fun createAuthIntent(): Intent {
        return accountManager.createAuthIntent()
    }

    fun handleAuthResult(intent: Intent?) {
        accountManager.handleAuthResult(
            intent,
            viewModelScope,
            updateState = { target -> _loginState.update { target } })
        if (_loginState.value !is LoginState.Error) {
            viewModelScope.launch {
                fetchUserInfo()
            }
        }
    }

    suspend fun fetchUserInfo() {
        val currentAccount = accountsRepository.activeAccount.first()
        val user = redditApi.getIdentity()
        if (user.isSuccess) {
            val identity = user.getOrNull()!!
            Log.d("LoginViewModel", "Updating ${currentAccount.id}")
            accountsRepository.updateAccount(
                currentAccount.id,
                currentAccount.copy(
                    info = identity,
                )
            )

        } else {
            val err = user.exceptionOrNull()!!
            Log.e("LoginViewModel", "Failed to get user info: $err")
            //accountsRepository.deleteAccount(accounts.size)
            _loginState.update { LoginState.Error(err) }
        }
    }
}

@Composable
fun AccountTile(
    account: RedditAccount,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
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
    ListItem(
        content = { Text(account.info?.username ?: "Anonymous") },
        selected = false,
        onClick = onClick,
        leadingContent = image,
        modifier = modifier,
    )
}
