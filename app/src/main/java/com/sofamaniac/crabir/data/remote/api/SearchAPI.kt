package com.sofamaniac.crabir.data.remote.api

import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.SortInterface
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.domain.model.Fullname
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
        @Query("after") after: Fullname? = null,
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
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
        @Query("type") type: String,
    ): Response<Listing<Thing>>

    @GET("api/search_subreddits")
    suspend fun searchSubreddits(
        @Query("query") query: String,
        @Query("include_over_18") includeOver18: Boolean = false,
        @Query("exact") exact: Boolean = false,
    ): Response<Listing<Thing>>
}

interface SearchSort : SortInterface


enum class CommunitySearchSort : SearchSort {
    Relevance {
        override val representation: Int = R.string.SortRelevance
    },
    Activity {
        override val representation: Int = R.string.SortActivity
    };

    override fun toString(): String {
        return super.toString().lowercase()
    }

    override val isTimeframe: Boolean = false
}

enum class PostSearchSort : SearchSort {
    Relevance {
        override val representation: Int = R.string.SortRelevance
    },
    Hot {
        override val representation: Int = R.string.SortHot
    },
    Comments {
        override val representation: Int = R.string.SortComments
    },
    Top {
        override val representation: Int = R.string.SortTop
        override val isTimeframe: Boolean = false
    },
    New {
        override val representation: Int = R.string.SortNew
        override val isTimeframe: Boolean = false
    };

    override fun toString(): String {
        return super.toString().lowercase()
    }

    override val isTimeframe: Boolean = true
}