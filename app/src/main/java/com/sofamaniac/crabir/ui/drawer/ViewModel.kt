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
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.auth.AuthConfig
import com.sofamaniac.crabir.data.remote.reddit.auth.BasicAuthClient
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    private val subredditDao: SubredditDao,
    private val multiDao: MultiDao,
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

    val subscriptions: StateFlow<List<Thing.Subreddit>> = subsRepository.subscriptions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList<Thing.Subreddit>()
    )
    val multis: StateFlow<List<Thing.Multi>> = subsRepository.multis.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList<Thing.Multi>()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            for (accounts in accountsRepository.accounts.first()) {
                if (accounts.info == null || accounts.isAnonymous() || accounts.isUninitialized()) {
                    accountsRepository.deleteAccount(accounts.id)
                }
            }
            val activeAccount = accountsRepository.activeAccount.first()
            if (activeAccount.isAnonymous() || accountsRepository.accounts.first().isEmpty()) {
                setupAnonymous()
                return@launch
            }
            if (activeAccount.info?.username.isNullOrBlank()) {
                fetchUserInfo()
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = redditApi.logout(activeAccount.first().auth.refreshToken!!)
                if (res.isSuccessful) {
                    accountsRepository.deleteAccount(activeAccount.first().id)
                } else {
                    throw Exception("Failed to logout: ${res.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to logout: $e")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun setupAnonymous() {
        if (accountsRepository.accounts.first().any { it.isAnonymous() && it.auth.isAuthorized }) {
            Log.i("LoginViewModel", "Anonymous account already set up")
            return
        }
        Log.d("LoginViewModel", "Setting up anonymous")
        val response = redditApi.getAnonymousAccessToken(
            deviceId = Uuid.random().toHexDashString()
        )
        if (response.isSuccessful) {
            Log.d("LoginViewModel", "Obtained access token for anonymous")
            val accessToken = response.body()!!
            val authResponse =
                AuthorizationResponse.Builder(serviceConfig.createAuthorizationRequest())
                    .setAccessToken(accessToken.access_token)
                    .setAccessTokenExpiresIn(accessToken.expires_in)
                    .build()
            val tokenRequest = TokenRequest.Builder(
                serviceConfig.authorizationServiceConfiguration(),
                BuildConfig.REDDIT_CLIENT_ID
            ).setGrantType(
                TokenRequest.GRANT_TYPE_CLIENT_CREDENTIALS
            ).build()
            val tokenResponse =
                TokenResponse.Builder(tokenRequest).setAccessToken(accessToken.access_token)
                    .setAccessTokenExpiresIn(accessToken.expires_in ?: Long.MAX_VALUE)
                    .setScope(accessToken.scope)
                    .build()
            val state = AuthState(authResponse, tokenResponse, null)
            val account = RedditAccount.anonymous().copy(auth = state)
            accountsRepository.addAccount(account)
            accountsRepository.setActiveAccount(-1)
        } else {
            Log.e("LoginViewModel", "failed to setup anonymous: ${response.errorBody()}")
        }
    }

    fun setActiveAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountsRepository.setActiveAccount(accountId)
            val account = activeAccount.first()
            Log.d(
                "LoginViewModel",
                "Setting active account to '${activeAccount.first().info?.username ?: "Anonymous"}'"
            )
            if (account.isAnonymous()) {
                if (account.auth.accessToken.isNullOrBlank()) {
                    setupAnonymous()
                }
                return@launch
            } else if (account.info?.username.isNullOrBlank()) {
                fetchUserInfo()
            }
        }
    }

    fun createAuthIntent(): Intent {
        val authRequest = serviceConfig.createAuthorizationRequest()
        val intent = authService.getAuthorizationRequestIntent(authRequest)
        Log.d("LoginViewModel", "Creating auth intent: $intent")
        return intent
    }

    fun handleAuthResult(intent: Intent?) {
        Log.d("LoginViewModel", "Handling auth result ${intent?.data}")
        if (intent == null) {
            _loginState.value = LoginState.Error("Login cancelled")
            return
        }

        val authResponse = AuthorizationResponse.fromIntent(intent)
        val authException = AuthorizationException.fromIntent(intent)

        Log.d("LoginViewModel", "Auth response: $authResponse")


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
        val clientAuth = BasicAuthClient
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
                    Log.d("LoginViewModel", "Got authorization token")
                    val authState = AuthState(authResponse, tokenResponse, null)
                    runBlocking(Dispatchers.IO) {
                        save(authState)
                    }
                }

                else -> {
                    Log.e("LoginViewModel", "Something went wrong")
                }
            }
        }
    }

    private suspend fun fetchUserInfo() {
        val currentAccount = accountsRepository.activeAccount.first()
        val user = redditApi.getIdentity()
        if (user.isSuccessful) {
            val identity = user.body()!!
            Log.d("LoginViewModel", "Updating ${currentAccount.id}")
            accountsRepository.updateAccount(
                currentAccount.id,
                currentAccount.copy(
                    info = identity,
                )
            )

        } else {
            Log.e("LoginViewModel", "Failed to get user info: ${user.message()}")
            //accountsRepository.deleteAccount(accounts.size)
        }
    }

    fun visitCommunity(data: SubredditData) {
        viewModelScope.launch(Dispatchers.IO) {
            subredditDao.upsert(data)
        }
    }

    fun visitCommunity(data: MultiData) {
        viewModelScope.launch(Dispatchers.IO) {
            multiDao.upsert(data)
        }
    }

    private suspend fun save(authState: AuthState) {
        try {
            val id = (accountsRepository.accounts.first().maxByOrNull { it.id }?.id ?: 0) + 1
            val newAccount = RedditAccount.uninitialized(id, authState)
            accountsRepository.addAccount(newAccount)
            accountsRepository.setActiveAccount(id)
            Log.d("LoginViewModel", "save: fetching user info")
            fetchUserInfo()
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Failed to save account: $e")
            _loginState.value = LoginState.Error("Failed to save account.")
        }
    }
}
