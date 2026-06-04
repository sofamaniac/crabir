package com.sofamaniac.crabir.data.remote.reddit.auth

import android.util.Base64
import com.sofamaniac.crabir.BuildConfig
import net.openid.appauth.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditAuthApi {

    @FormUrlEncoded
    @POST("access_token")
    fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Header("Authorization") basicAuth: String = authorizationHeader,
    ): Call<TokenResponse>

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/access_token")
    suspend fun getAccessToken(@Field("grant_type") grantType: String = "client_credentials")

    @FormUrlEncoded
    @POST("https://www.reddit.com/api/v1/revoke_token")
    suspend fun logout(
        @Field("token") token: String,
        //@Field("token_type_hint") tokenTypeHint: String = "access_token",
        @Header("Authorization") basicAuth: String = authorizationHeader,
    ): Response<Unit>
}

val authorizationHeader =
    "Basic " + Base64.encodeToString(
        "${BuildConfig.REDDIT_CLIENT_ID}:".toByteArray(),
        Base64.NO_WRAP
    )
