package com.sofamaniac.reboost.data.remote.api.auth

import android.util.Base64
import com.sofamaniac.reboost.BuildConfig
import net.openid.appauth.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RedditAuthApi {

    @FormUrlEncoded
    @POST("access_token")
    fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Header("Authorization") basicAuth: String = authorizationHeader,
    ): Call<TokenResponse>

    @POST("/api/v1/revoke_token")
    suspend fun logout(
        @Query("token") token: String,
        @Query("token_type_hint") tokenTypeHint: String = "access_token",
        @Header("Authorization") basicAuth: String = authorizationHeader,
    ): Response<Unit>
}

val authorizationHeader =
    "Basic " + Base64.encodeToString(
        "${BuildConfig.REDDIT_CLIENT_ID}:".toByteArray(),
        Base64.NO_WRAP
    )
