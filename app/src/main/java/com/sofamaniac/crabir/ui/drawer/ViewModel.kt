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
import com.sofamaniac.crabir.AccountManager
import com.sofamaniac.crabir.data.local.dao.MultiRepository
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
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
import net.openid.appauth.AuthorizationService
import org.koin.core.annotation.InjectedParam
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
    abstract fun setActiveAccount(accountId: Int)
    abstract fun toggleSelectAccount()
    abstract fun logout()
    abstract fun createAuthIntent(): Intent
    abstract fun visitCommunity(data: SubredditData)
    abstract fun visitCommunity(data: MultiData)
    abstract fun handleAuthResult(intent: Intent?)
}

@KoinViewModel(binds = [DrawerViewModel::class])
class DrawerViewModelImpl(
    private val authService: AuthorizationService,
    private val accountsRepository: AccountsRepository,
    private val subsRepository: SubscriptionsRepository,
    private val redditApi: RedditAPIService,
    private val subredditDao: SubredditRepository,
    private val multiDao: MultiRepository,
    private val accountManager: AccountManager,
    @InjectedParam private val clientId: String,
) : DrawerViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    override val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    private var initialized = false

    private val flowManager =
        AuthFlowManager(
            authService,
            accountsRepository,
            redditApi,
            updateState = { newVal -> _loginState.update { newVal } },
            clientId = clientId,
        )

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
            initialValue = emptyList<Thing.Subreddit>()
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
        initialValue = emptyList<Thing.Multi>()
    )


    override fun initialize() {
        if (initialized) return
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.initialize { err ->
                _loginState.update { LoginState.Error(err) }
            }
            val account = activeAccount.first()
            if (!account.isAnonymous() && !account.isUninitialized()) {
                fetchUserInfo()
            }
            initialized = true
        }
    }

    override fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = redditApi.logout(activeAccount.first().auth.refreshToken!!)
            if (res.isSuccess) {
                accountsRepository.deleteAccount(activeAccount.first().id)
            } else {
                val error = res.exceptionOrNull()!!
                Log.e("LoginViewModel", "Failed to logout: $error")
                _loginState.update { LoginState.Error(error) }
            }
        }
    }


    override fun setActiveAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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

    override fun createAuthIntent(): Intent {
        return flowManager.createAuthIntent()
    }

    override fun handleAuthResult(intent: Intent?) {
        flowManager.handleAuthResult(intent, viewModelScope)
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

    override fun visitCommunity(data: SubredditData) {
        viewModelScope.launch(Dispatchers.IO) {
            subredditDao.upsert(data)
        }
    }

    override fun visitCommunity(data: MultiData) {
        viewModelScope.launch(Dispatchers.IO) {
            multiDao.upsert(data)
        }
    }
}