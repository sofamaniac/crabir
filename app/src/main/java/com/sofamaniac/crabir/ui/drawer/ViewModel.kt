/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:46 PM
 *
 */

package com.sofamaniac.crabir.ui.drawer

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
import com.sofamaniac.crabir.domain.repository.accounts.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: Throwable) : LoginState()
}

abstract class DrawerViewModel : ViewModel() {
    abstract val accountsList: Flow<List<RedditAccount>>
    abstract val otherAccounts: Flow<List<RedditAccount>>
    abstract val activeAccount: Flow<RedditAccount>
    abstract val loginState: StateFlow<LoginState>
    abstract val selectingAccount: StateFlow<Boolean>
    abstract val subscriptions: StateFlow<List<Thing.Subreddit>>
    abstract val sortedSubscriptions: StateFlow<List<Thing.Subreddit>>
    abstract val multis: StateFlow<List<Thing.Multi>>
    abstract fun initialize()
    abstract suspend fun setActiveAccount(accountId: Int)
    abstract fun toggleSelectAccount()
    abstract fun logout()
    abstract fun createAuthIntent(): Intent
    abstract fun handleAuthResult(intent: Intent?)
}

@KoinViewModel(binds = [DrawerViewModel::class])
class DrawerViewModelImpl(
    private val accountsRepository: AccountsRepository,
    subsRepository: SubscriptionsRepository,
) : DrawerViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    override val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    private var initialized = false

    override val accountsList = accountsRepository.accounts
    override val activeAccount = accountsRepository.activeAccount
    override val otherAccounts = combine(
        accountsRepository.accounts,
        accountsRepository.activeAccount
    ) { accounts, active ->
        accounts.filterNot { it.id == active.id || it.isAnonymous() }
            .sortedByDescending { it.id }
    }

    private val _selectingAccount = MutableStateFlow(false)
    override val selectingAccount = _selectingAccount.asStateFlow()

    override fun toggleSelectAccount() {
        _selectingAccount.value = !_selectingAccount.value
    }


    override val subscriptions: StateFlow<List<Thing.Subreddit>> =
        subsRepository.subscriptions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    override val sortedSubscriptions: StateFlow<List<Thing.Subreddit>> =
        subsRepository.subscriptions
            .map { subs ->
                subs.sortedWith(
                    compareByDescending<Thing.Subreddit> { it.data.userHasFavorited }
                        .thenBy { it.data.displayName.lowercase() }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
    override val multis: StateFlow<List<Thing.Multi>> = subsRepository.multis.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )


    override fun initialize() {
        if (initialized) return
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.initialize { err ->
                _loginState.update { LoginState.Error(err) }
            }
            initialized = true
        }
    }

    override fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.logout(activeAccount.first().id).onFailure { err ->
                Log.e("LoginViewModel", "Failed to logout: $err")
                _loginState.update { LoginState.Error(err) }
            }
        }
    }


    override suspend fun setActiveAccount(accountId: Int) {
        if (activeAccount.first().id == accountId) return
        accountsRepository.setActiveAccount(accountId).onFailure { err ->
            Log.e("LoginViewModel", "Failed to set active account:", err)
            _loginState.update { LoginState.Error(err) }
        }
    }

    override fun createAuthIntent(): Intent {
        return accountsRepository.createAuthIntent()
    }

    override fun handleAuthResult(intent: Intent?) {
        accountsRepository.handleAuthResult(
            intent,
            viewModelScope,
            updateState = { target -> _loginState.update { target } }
        )
    }
}
