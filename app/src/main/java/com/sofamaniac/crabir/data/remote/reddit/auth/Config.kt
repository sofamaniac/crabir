/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.reddit.auth

import android.util.Base64
import androidx.core.net.toUri
import com.sofamaniac.crabir.BuildConfig
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ResponseTypeValues
import kotlin.enums.enumEntries

class AuthConfig(
) {
    val authorizationEndpoint = "https://sh.reddit.com/api/v1/authorize.compact"
    private val tokenEndpoint = "https://www.reddit.com/api/v1/access_token"
    private val redirectUri = "com.sofamaniac.crabir://callback"
    private val clientId = BuildConfig.REDDIT_CLIENT_ID

    /** Add all available scopes */
    private val scopes = enumEntries<Scopes>().map { it.name.lowercase() }

    fun authorizationServiceConfiguration(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            authorizationEndpoint.toUri(),
            tokenEndpoint.toUri()
        )
    }

    fun createAuthorizationRequest(): AuthorizationRequest {
        val serviceConfiguration = authorizationServiceConfiguration()
        return AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri.toUri(),
        )
            .setScopes(scopes)
            .setAdditionalParameters(mapOf<String?, String?>("duration" to "permanent"))
            .build()
    }
}

//val BasicAuthClient = ClientSecretBasic(BuildConfig.REDDIT_CLIENT_ID)

val BasicAuthClient = object : ClientAuthentication {
    override fun getRequestHeaders(clientId: String): MutableMap<String, String> {
        return mutableMapOf(
            "Authorization" to "Basic " + Base64.encodeToString(
                "$clientId:".toByteArray(),
                Base64.NO_WRAP
            )
        )
    }

    override fun getRequestParameters(clientId: String): Map<String?, String?> {
        return mutableMapOf()
    }
}

