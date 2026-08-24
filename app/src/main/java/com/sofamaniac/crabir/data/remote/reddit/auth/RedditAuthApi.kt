package com.sofamaniac.crabir.data.remote.reddit.auth

import android.util.Base64
import com.sofamaniac.crabir.BuildConfig
import kotlinx.serialization.Serializable
import net.openid.appauth.TokenResponse
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditAuthApi {

    @FormUrlEncoded
    @POST("access_token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Header("Authorization") basicAuth: String = authorizationHeader,
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/access_token")
    @NoAuth
    suspend fun getAccessToken(@Field("grant_type") grantType: String = "client_credentials"): Response<AccessTokenResponse>

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/access_token")
    @NoAuth
    suspend fun getAnonymousAccessToken(
        @Field("grant_type") grantType: String = "https://oauth.reddit.com/grants/installed_client",
        @Field("device_id") deviceId: String,
        @Field("duration") duration: String = "permanent"
    ): Response<AccessTokenResponse>

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/revoke_token")
    suspend fun logout(
        @Field("token") token: String,
        @Header("Authorization") basicAuth: String = authorizationHeader,
    ): Response<Unit>
}

@Retention(AnnotationRetention.RUNTIME)
annotation class NoAuth

fun Request.isUnauthenticated(): Boolean {
    return this.tag(Invocation::class.java)?.method()?.annotations?.any { it is NoAuth } ?: false
}

@Serializable
data class AccessTokenResponse(
    val access_token: String,
    val device_id: String?,
    val expires_in: Long?,
    val scope: String,
    val token_type: String
)

val authorizationHeader =
    "Basic " + Base64.encodeToString(
        "${BuildConfig.REDDIT_CLIENT_ID}:".toByteArray(),
        Base64.NO_WRAP
    )
