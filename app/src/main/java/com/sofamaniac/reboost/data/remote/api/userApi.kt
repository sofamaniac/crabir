/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.api

import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.user.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.sofamaniac.reboost.data.remote.dto.Timeframe as PostTimeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort as PostSort


interface UserAPI {

    @GET("/user/{username}/about.json")
    suspend fun getUser(@Path("username") username: String): User

    @GET("api/v1/me.json")
    suspend fun getIdentity(): Response<User>

    @GET("user/{user}/saved.json")
    suspend fun getSaved(
        @Path("user") user: String,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/submitted.json")
    suspend fun getSubmitted(
        @Path("user") user: String,
        @Query("sort") sort: PostSort = PostSort.New,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/comments.json")
    suspend fun getComments(
        @Path("user") user: String,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/hidden.json")
    suspend fun getHidden(
        @Path("user") user: String,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/upvoted.json")
    suspend fun getUpvoted(
        @Path("user") user: String,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/downvoted.json")
    suspend fun getDownvoted(
        @Path("user") user: String,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>
}

