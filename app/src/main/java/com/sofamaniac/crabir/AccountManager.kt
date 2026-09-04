package com.sofamaniac.crabir

import android.content.Context
import android.util.Log
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.auth.AuthConfig
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.settings.api.apiSettingsDataStore
import kotlinx.coroutines.flow.first
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
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
) {
    private var initialized = false
    suspend fun initialize(onError: (Throwable) -> Unit) {
        if (initialized) return
        val apiSettings = context.apiSettingsDataStore.data.first()
        if (apiSettings.redditClientId == null) return
        setupAnonymous(apiSettings.redditClientId, onError)
        initialized = true
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun setupAnonymous(clientId: String, onError: (Throwable) -> Unit) {
        Log.d("AccountManager", "Setting up anonymous")
        val response = redditApi.getAnonymousAccessToken(
            deviceId = Uuid.random().toHexDashString()
        )
        if (response.isSuccess) {
            Log.d("AccountManager", "Obtained access token for anonymous")
            val serviceConfig = AuthConfig(clientId)
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
}