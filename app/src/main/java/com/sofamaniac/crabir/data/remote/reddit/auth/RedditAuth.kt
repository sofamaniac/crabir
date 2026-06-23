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
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.coroutines.resume


class RedditAuthenticator @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication,
) : Interceptor {

    private val activeAccount: Flow<RedditAccount> = accountsRepository.activeAccount

    override fun intercept(chain: Interceptor.Chain): Response {
        val activeAccount = runBlocking { activeAccount.first() }
        Log.d("RedditAuthenticator", "Authenticating with ${activeAccount.auth.accessToken}")

//        if (activeAccount.isAnonymous()) {
//            Log.w("RedditAuthenticator", "Anonymous account")
//            // TODO
//            //return null // Anonymous account
//            Log.d("RedditAuthenticator", "auth: ${activeAccount.auth.accessToken}")
//            Log.d("RedditAuthenticator", "${chain.request().headers}")
//
//            return chain.proceed(chain.request())
//        } else
        if (chain.request().url.host.contains("www.reddit.com")) {
            // Disable auth on non oauth endpoints
            Log.w("RedditAuthenticator", "Non oauth endpoint (${chain.request().url})")
            val request =
                chain.request().newBuilder().header("Authorization", authorizationHeader).build()
            return chain.proceed(request)
        }

        val newAccessToken = if (activeAccount.auth.needsTokenRefresh) {
            refreshToken(activeAccount)
        } else {
            activeAccount.auth.accessToken
        }
        if (activeAccount.auth.accessToken == null) {
            chain.proceed(chain.request())
        }

        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $newAccessToken").build()

        return chain.proceed(request)
    }

    private fun refreshToken(account: RedditAccount): String? {
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