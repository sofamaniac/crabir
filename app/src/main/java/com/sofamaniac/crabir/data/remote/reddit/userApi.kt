/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.profile.ProfileSort
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface UserAPI {

    @GET("/user/{username}/about.json")
    suspend fun getUser(@Path("username") username: String): Response<Thing.User>

    @GET("api/v1/me.json")
    suspend fun getIdentity(): Response<UserDTO>

    @GET("user/{user}/saved.json")
    suspend fun getSaved(
        @Path("user") user: String,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/overview.json")
    suspend fun getOverview(
        @Path("user") user: String,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/submitted.json")
    suspend fun getSubmitted(
        @Path("user") user: String,
        @Query("sort") sort: ProfileSort = ProfileSort.New,
        @Query("t") timeframe: Timeframe? = null,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/comments.json")
    suspend fun getComments(
        @Path("user") user: String,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
        @Query("sort") sort: ProfileSort = ProfileSort.Top,
        @Query("t") timeframe: Timeframe? = null,
    ): Response<Listing<Thing>>

    @GET("user/{user}/hidden.json")
    suspend fun getHidden(
        @Path("user") user: String,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/upvoted.json")
    suspend fun getUpvoted(
        @Path("user") user: String,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @GET("user/{user}/downvoted.json")
    suspend fun getDownvoted(
        @Path("user") user: String,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Thing>>

    @POST("api/block")
    suspend fun block(username: String): Response<Unit>
}

