package com.sofamaniac.reboost.data.remote.api

import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchAPI {
    @GET("/r/{subreddit}/search.json")
    suspend fun search(
        @Path("subreddit") subreddit: String = "",
        @Query("restrict_sr") restrictSubreddit: Boolean = true,
        @Query("q") query: String,
        @Query("sort") sort: SearchSort = PostSearchSort.Relevance,
        @Query("t") timeframe: Timeframe? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
        @Query("type") type: String,
    ): Response<Listing<Thing>>

    @GET("search.json")
    suspend fun search(
        @Query("q") query: String,
        @Query("sort") sort: SearchSort = PostSearchSort.Relevance,
        @Query("t") timeframe: Timeframe? = null,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
        @Query("type") type: String,
    ): Response<Listing<Thing>>
}

interface SearchSort


enum class CommunitySearchSort : SearchSort {
    Relevance,
    Activity;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}

enum class PostSearchSort : SearchSort {
    Relevance,
    Hot,
    Top,
    New,
    Comments;

    override fun toString(): String {
        return super.toString().lowercase()
    }

    fun isTimeframe(): Boolean {
        return when (this) {
            Relevance, Hot, Comments -> true
            else -> false
        }
    }
}