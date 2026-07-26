package com.sofamaniac.crabir

import android.util.Log
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.auth.AuthConfig
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import org.koin.core.annotation.Single
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class AccountManager(val accountsRepository: AccountsRepository, val redditApi: RedditAPIService) {
    private var initialized = false
    suspend fun initialize(onError: (Throwable) -> Unit) {
        if (initialized) return
        setupAnonymous(onError)
        initialized = true
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun setupAnonymous(onError: (Throwable) -> Unit) {
        Log.d("AccountManager", "Setting up anonymous")
        val response = try {
            redditApi.getAnonymousAccessToken(
                deviceId = Uuid.random().toHexDashString()
            )
        } catch (e: Exception) {
            onError(e)
            return
        }
        if (response.isSuccessful) {
            Log.d("AccountManager", "Obtained access token for anonymous")
            val serviceConfig = AuthConfig()
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
        } else {
            val error = response.errorBody()
            Log.e("AccountManager", "failed to setup anonymous: ${error}")
            onError(
                Exception(
                    error?.string() ?: "Something went wrong"
                )
            )
        }
    }
}