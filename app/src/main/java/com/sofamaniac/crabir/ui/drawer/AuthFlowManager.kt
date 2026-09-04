package com.sofamaniac.crabir.ui.drawer

import android.content.Intent
import android.util.Log
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.auth.AuthConfig
import com.sofamaniac.crabir.data.remote.reddit.auth.BasicAuthClient
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

class AuthFlowManager(
    private val authService: AuthorizationService,
    private val accountsRepository: AccountsRepository,
    private val redditApi: RedditAPIService,
    private val updateState: (LoginState) -> Unit,
    private val clientId: String,
) {
    val serviceConfig = AuthConfig(clientId)
    fun createAuthIntent(): Intent {
        val authRequest = serviceConfig.createAuthorizationRequest()
        val intent = authService.getAuthorizationRequestIntent(authRequest)
        Log.d("LoginViewModel", "Creating auth intent: $intent")
        return intent
    }

    fun handleAuthResult(intent: Intent?, scope: CoroutineScope) {
        Log.d("LoginViewModel", "Handling auth result ${intent?.data}")
        if (intent == null) {
            updateState(LoginState.Error(Exception("Login cancelled")))
            return
        }

        val authResponse = AuthorizationResponse.fromIntent(intent)
        val authException = AuthorizationException.fromIntent(intent)

        Log.d("LoginViewModel", "Auth response: $authResponse")


        when {
            authException != null -> {
                Log.e("LoginViewModel", "Authorization exception: $authException")
                updateState(LoginState.Error(authException))
            }

            authResponse != null -> {
                updateState(LoginState.Loading)
                exchangeAuthCodeForToken(authResponse, scope)
            }
        }
    }


    private fun exchangeAuthCodeForToken(
        authResponse: AuthorizationResponse,
        scope: CoroutineScope,
    ) {
        val clientAuth = BasicAuthClient
        authService.performTokenRequest(
            authResponse.createTokenExchangeRequest(),
            clientAuth
        ) { tokenResponse, ex ->
            when {
                ex != null -> {
                    Log.e("LoginViewModel", "Token exchange failed: $ex")
                    updateState(LoginState.Error(ex))
                }

                tokenResponse != null -> {
                    Log.d("LoginViewModel", "Got authorization token")
                    val authState = AuthState(authResponse, tokenResponse, null)
                    scope.launch(Dispatchers.IO) {
                        save(authState)
                    }
                }

                else -> {
                    Log.e("LoginViewModel", "Something went wrong")
                }
            }
        }
    }


    private suspend fun save(authState: AuthState) {
        try {
            val id = (accountsRepository.accounts.first().maxByOrNull { it.id }?.id ?: 0) + 1
            val newAccount = RedditAccount.uninitialized(id, authState)
            accountsRepository.addAccount(newAccount)
            accountsRepository.setActiveAccount(id)
            Log.d("LoginViewModel", "save: fetching user info")
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Failed to save account: $e")
            updateState(LoginState.Error(e))
        }
    }
}