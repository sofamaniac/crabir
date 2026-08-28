package com.sofamaniac.crabir.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface RandditAPI {
    @GET("/random")
    suspend fun getRandomCommunity(@Query("include_nsfw") includeNsfw: Boolean): Result<RandditResponse>
}

@Serializable
data class RandditResponse(val url: String, val over18: Boolean)