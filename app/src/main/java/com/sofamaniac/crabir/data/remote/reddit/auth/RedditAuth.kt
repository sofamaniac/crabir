/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:59 PM
 *
 */

package com.sofamaniac.crabir.data.remote.reddit.auth

import android.util.Log
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import org.koin.core.annotation.Singleton
import kotlin.coroutines.resume


@Singleton
class RedditAuthenticator(
    private val accountsRepository: AccountsRepository,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication,
) : Interceptor {

    private val activeAccount: Flow<RedditAccount> = accountsRepository.activeAccount

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val overrideAccount = request.tag(RedditAccount::class.java)
        val activeAccount = overrideAccount ?: runBlocking { activeAccount.first() }

        if (request.isUnauthenticated()) {
            // Disable auth on non oauth endpoints
            Log.w("RedditAuthenticator", "Non oauth endpoint (${request.url})")
            val request =
                chain.request().newBuilder().header("Authorization", authorizationHeader).build()
            return chain.proceed(request)
        } else if (activeAccount.auth.accessToken == null) {
            Log.e("RedditAuthenticator", "No access token found")
            //chain.proceed(chain.request())
            throw IOException("No authentication found")
        }
        val newAccessToken = if (activeAccount.auth.needsTokenRefresh) {
            refreshToken(activeAccount)
        } else {
            activeAccount.auth.accessToken
        }

        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken").build()

        return chain.proceed(newRequest)
    }

    private fun refreshToken(account: RedditAccount): String? {
        Log.d("RedditAuthenticator", "Refreshing token for ${account.id}")
        return try {
            runBlocking<String?> {
                suspendCancellableCoroutine { continuation ->
                    account.auth.performActionWithFreshTokens(
                        authService,
                        clientAuth
                    ) { accessToken, _, ex ->
                        if (ex != null) {
                            Log.e("RedditAuthenticator", "Token refresh failed", ex)
                            continuation.resume(null)
                        } else {
                            continuation.resume(accessToken)
                        }
                    }
                }
            }.also { token ->
                if (token != null) {
                    runBlocking {
                        accountsRepository.updateAuthState(account.id, account.auth)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RedditAuthenticator", "Exception during token refresh", e)
            null
        }
    }
}