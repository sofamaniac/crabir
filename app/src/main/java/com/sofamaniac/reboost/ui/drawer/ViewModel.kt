/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:46 PM
 *
 */

package com.sofamaniac.reboost.ui.drawer

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.api.auth.AuthConfig
import com.sofamaniac.reboost.data.remote.api.auth.BasicAuthClient
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.SubscriptionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val authService: AuthorizationService,
    private val accountsRepository: AccountsRepository,
    private val subsRepository: SubscriptionsRepository,
    private val redditApi: RedditAPIService,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()


    val accountsList = accountsRepository.accounts
    val activeAccount = accountsRepository.activeAccount

    private val _selectingAccount = MutableStateFlow(false)
    val selectingAccount = _selectingAccount.asStateFlow()

    fun toggleSelectAccount() {
        _selectingAccount.value = !_selectingAccount.value
    }

    val serviceConfig = AuthConfig()

    val subscriptions: StateFlow<List<Thing.Subreddit>>
        get() = subsRepository.subscriptions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<Thing.Subreddit>()
        )
    val multis: StateFlow<List<Thing.Multi>>
        get() = subsRepository.multis.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<Thing.Multi>()
        )

    fun logout() {
        viewModelScope.launch {
            try {
                redditApi.logout(activeAccount.first().auth.accessToken!!)
                accountsRepository.deleteAccount(activeAccount.first().id)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to logout: $e")
            }
        }
    }

    fun setActiveAccount(accountId: Int) {
        viewModelScope.launch {
            accountsRepository.setActiveAccount(accountId)
        }
    }

    fun createAuthIntent(): Intent {
        val authRequest = serviceConfig.createAuthorizationRequest()
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    fun handleAuthResult(data: Intent?) {
        if (data == null) {
            _loginState.value = LoginState.Error("Login cancelled")
            return
        }

        val authResponse = AuthorizationResponse.fromIntent(data)
        val authException = AuthorizationException.fromIntent(data)

        when {
            authException != null -> {
                Log.e("LoginViewModel", "Authorization exception: $authException")
                _loginState.value = LoginState.Error(
                    authException.message ?: "Authorization exception: $authException"
                )
            }

            authResponse != null -> {
                _loginState.value = LoginState.Loading
                exchangeAuthCodeForToken(authResponse)
            }
        }
    }

    private fun exchangeAuthCodeForToken(authResponse: AuthorizationResponse) {
        val clientAuth = BasicAuthClient()
        authService.performTokenRequest(
            authResponse.createTokenExchangeRequest(),
            clientAuth
        ) { tokenResponse, ex ->
            when {
                ex != null -> {
                    Log.e("LoginViewModel", "Token exchange failed: $ex")
                    _loginState.value = LoginState.Error("Failed to get access token.")
                }

                tokenResponse != null -> {
                    val authState = AuthState(authResponse, tokenResponse, null)
                    save(authState)
                }

            }
        }
    }

    private fun save(authState: AuthState) {
        viewModelScope.launch {
            try {
                val accounts = accountsRepository.accounts.first()
                val newAccount = RedditAccount.unitialized(accounts.size, authState)
                accountsRepository.addAccount(newAccount)
                accountsRepository.setActiveAccount(accounts.size)
                try {
                    val user = redditApi.getIdentity()
                    if (user.isSuccessful) {
                        val identity = user.body()!!
                        accountsRepository.updateAccount(
                            accounts.size,
                            newAccount.copy(
                                username = identity.name,
                                thumbnailUrl = identity.iconImg
                            )
                        )
                    } else {
                        Log.e("LoginViewModel", "Failed to get user info: ${user.message()}")
                        accountsRepository.deleteAccount(accounts.size)
                    }
                } catch (e: Exception) {
                    Log.e("LoginViewModel", "Failed to get user info: $e")
                    accountsRepository.deleteAccount(accounts.size)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to save account: $e")
                _loginState.value = LoginState.Error("Failed to save account.")
            }
        }
    }
}
