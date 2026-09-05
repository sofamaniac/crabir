package com.sofamaniac.crabir

import android.content.Context
import android.content.Intent
import android.util.Log
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.auth.AuthConfig
import com.sofamaniac.crabir.data.remote.reddit.auth.BasicAuthClient
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.settings.api.apiSettingsDataStore
import com.sofamaniac.crabir.ui.drawer.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import org.koin.core.annotation.Single
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class AccountManager(
    val accountsRepository: AccountsRepository,
    val redditApi: RedditAPIService,
    private val context: Context,
    private val authService: AuthorizationService,
) {
    private var initialized = false
    private var clientId = ""
    suspend fun initialize(onError: (Throwable) -> Unit) {
        if (initialized) return
        val apiSettings = context.apiSettingsDataStore.data.first()
        if (apiSettings.redditClientId == null || apiSettings.redditRedirectUri == null) return
        clientId = apiSettings.redditClientId
        serviceConfig = AuthConfig(clientId, apiSettings.redditRedirectUri)
        Log.d("AccountManager", "Initializing account manager: $clientId")
        setupAnonymous(apiSettings.redditClientId, onError)
        initialized = true
    }

    private lateinit var serviceConfig: AuthConfig
    fun createAuthIntent(): Intent {
        if (!initialized) throw IllegalStateException("AccountManager not initialized")
        val authRequest = serviceConfig.createAuthorizationRequest()
        val intent = authService.getAuthorizationRequestIntent(authRequest)
        Log.d("LoginViewModel", "Creating auth intent: $intent")
        return intent
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun setupAnonymous(clientId: String, onError: (Throwable) -> Unit) {
        Log.d("AccountManager", "Setting up anonymous")
        val response = redditApi.getAnonymousAccessToken(
            deviceId = Uuid.random().toHexDashString()
        )
        if (response.isSuccess) {
            Log.d("AccountManager", "Obtained access token for anonymous")
            val accessToken = response.getOrNull()!!
            val authResponse =
                AuthorizationResponse.Builder(serviceConfig.createAuthorizationRequest())
                    .setAccessToken(accessToken.access_token)
                    .setAccessTokenExpiresIn(accessToken.expires_in)
                    .build()
            val tokenRequest = TokenRequest.Builder(
                serviceConfig.authorizationServiceConfiguration(),
                clientId,
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
        } else {
            val error = response.exceptionOrNull()
            Log.e("AccountManager", "failed to setup anonymous: ${error}")
            onError(
                error ?: Exception("Something went wrong")
            )
        }
    }

    fun handleAuthResult(
        intent: Intent?,
        scope: CoroutineScope,
        updateState: (LoginState) -> Unit,
    ) {
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
                exchangeAuthCodeForToken(authResponse, scope, updateState)
            }
        }
    }


    private fun exchangeAuthCodeForToken(
        authResponse: AuthorizationResponse,
        scope: CoroutineScope,
        updateState: (LoginState) -> Unit,
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
                        save(authState, updateState)
                    }
                }

                else -> {
                    Log.e("LoginViewModel", "Something went wrong")
                }
            }
        }
    }


    private suspend fun save(authState: AuthState, updateState: (LoginState) -> Unit) {
        try {
            val id = (accountsRepository.accounts.first().maxByOrNull { it.id }?.id ?: 0) + 1
            val newAccount = RedditAccount.uninitialized(id, authState)
            accountsRepository.addAccount(newAccount)
            accountsRepository.setActiveAccount(id)
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Failed to save account: $e")
            updateState(LoginState.Error(e))
        }
    }

}