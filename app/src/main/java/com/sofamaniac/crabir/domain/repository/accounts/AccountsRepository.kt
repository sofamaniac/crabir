package com.sofamaniac.crabir.domain.repository.accounts

import android.content.Context
import android.content.Intent
import android.util.Log
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.auth.BasicAuthClient
import com.sofamaniac.crabir.data.remote.reddit.auth.Config
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.settings.api.apiSettingsDataStore
import com.sofamaniac.crabir.ui.drawer.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
class AccountsRepository(
    private val accountsStorage: AccountsStorage,
    private val redditApi: RedditAPIService,
    private val context: Context,
    private val authService: AuthorizationService,
) {
    var initialized = false
        private set
    private var clientId = ""

    val accounts = accountsStorage.accounts
    val activeAccount = accountsStorage.activeAccount
    val activeAccountId = accountsStorage.activeAccountId

    suspend fun setActiveAccount(id: Int): Result<Unit> {
        accountsStorage.setActiveAccount(id)
        return fetchUserInfo(id)
    }

    suspend fun fetchUserInfo(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        if (id != RedditAccount.ANONYMOUS_ID) {
            redditApi.getIdentity().onSuccess { identity ->
                accountsStorage.updateAccount(id, activeAccount.first().copy(info = identity))
            }.map {}
        } else {
            Result.success(Unit)
        }
    }

    suspend fun initialize(onError: (Throwable) -> Unit) {
        if (initialized) return
        val apiSettings = context.apiSettingsDataStore.data.first()
        if (apiSettings.redditClientId == null || apiSettings.redditRedirectUri == null) return
        clientId = apiSettings.redditClientId
        serviceConfig = Config(clientId, apiSettings.redditRedirectUri)
        setupAnonymous(apiSettings.redditClientId, onError)
        initialized = true
    }

    suspend fun logout(id: Int): Result<Unit> {
        val account = accountsStorage.activeAccount.first()
        val token = account.auth.accessToken
        if (token == null) {
            return Result.failure(IllegalStateException("No token found"))
        }
        return redditApi.logout(token).fold(onSuccess = {
            accountsStorage.deleteAccount(id)
            Result.success(Unit)
        }, onFailure = { ex ->
            Result.failure(ex)
        })
    }

    private lateinit var serviceConfig: Config
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
                    .setAccessToken(accessToken.accessToken)
                    .setAccessTokenExpiresIn(accessToken.expiresIn)
                    .build()
            val tokenRequest = TokenRequest.Builder(
                serviceConfig.authorizationServiceConfiguration(),
                clientId,
            ).setGrantType(
                TokenRequest.GRANT_TYPE_CLIENT_CREDENTIALS
            ).build()
            val tokenResponse =
                TokenResponse.Builder(tokenRequest).setAccessToken(accessToken.accessToken)
                    .setAccessTokenExpiresIn(accessToken.expiresIn ?: Long.MAX_VALUE)
                    .setScope(accessToken.scope)
                    .build()
            val state = AuthState(authResponse, tokenResponse, null)
            val account = RedditAccount.anonymous().copy(auth = state)
            accountsStorage.addAccount(account)
            if (accountsStorage.activeAccountId.first() < 0) {
                accountsStorage.setActiveAccount(account.id)
            }
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
                        save(authState)
                    }
                }

                else -> {
                    Log.e("LoginViewModel", "Something went wrong")
                }
            }
        }
    }


    private suspend fun save(authState: AuthState) = withContext(Dispatchers.IO + NonCancellable) {
        val id =
            ((accounts.first().filter { it.id != RedditAccount.ANONYMOUS_ID }
                .maxByOrNull { it.id }?.id
                ?: 0) + 1).coerceAtLeast(0)
        val newAccount = RedditAccount.uninitialized(id, authState)
        accountsStorage.addAccount(newAccount)
        accountsStorage.setActiveAccount(id)
        Log.d("LoginViewModel", "Saving account $newAccount")
        redditApi.getIdentity().onSuccess { identity ->
            accountsStorage.updateAccount(id, newAccount.copy(info = identity))
        }.onFailure {
            Log.e("LoginViewModel", "Failed to get identity:", it)
        }
    }
}
