package com.sofamaniac.crabir

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.accounts.AccountsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AccountConfigurationViewModel(private val manager: AccountsRepository) : ViewModel() {
    val currentAccount =
        manager.activeAccount.dropWhile { account -> account.isUninitialized() }
            .distinctUntilChanged { old, new ->
                old.id == new.id
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                RedditAccount.anonymous()
            )
    val activeId = manager.activeAccountId.distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            -1
        )

    fun updateInfo() {
        viewModelScope.launch {
            manager.fetchUserInfo(activeId.value)
        }
    }

    init {
        viewModelScope.launch {
            manager.initialize { err ->
                Log.e("AccountConfigurationViewModel", "Failed to initialize: $err")
            }
        }
    }
}

@Composable
fun ConfigureAccount(
    onAccountChange: () -> Unit,
    viewModel: AccountConfigurationViewModel = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val currentAccount by viewModel.currentAccount.collectAsState()
    val activeId by viewModel.activeId.collectAsState()
    LaunchedEffect(activeId) {
        if (activeId > 0) {
            Log.d("ConfigureAccount", "Account changed: $activeId")
            onAccountChange()
            viewModel.updateInfo()
        }
    }
    val account = currentAccount
    CompositionLocalProvider(
        LocalRedditAccount provides account
    ) {
        content()
    }
}
