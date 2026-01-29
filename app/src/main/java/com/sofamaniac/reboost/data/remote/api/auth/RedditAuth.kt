/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:59 PM
 *
 */

package com.sofamaniac.reboost.data.remote.api.auth

import android.util.Log
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RedditAuthenticator @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication,
) : Interceptor {

    private val activeAccount: StateFlow<RedditAccount> = accountsRepository.activeAccount

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("RedditAuthenticator", "Authenticating with $activeAccount")
        val activeAccount = activeAccount.value

        if (activeAccount.isAnonymous()) {
            Log.w("RedditAuthenticator", "Anonymous account")
            // TODO
            //return null // Anonymous account
            return chain.proceed(chain.request())
        }

        val newAccessToken = if (activeAccount.auth.needsTokenRefresh) {
            refreshToken(activeAccount)
        } else {
            activeAccount.auth.accessToken
        }

        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $newAccessToken").build()

        return chain.proceed(request)
    }

    private fun refreshToken(account: RedditAccount): String? {
        return try {
            runBlocking<String?> {
                suspendCoroutine { continuation ->
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