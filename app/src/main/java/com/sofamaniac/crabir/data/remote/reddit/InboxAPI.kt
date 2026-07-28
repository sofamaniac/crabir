package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.Fullname
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface InboxAPI {
    @GET("/message/inbox.json")
    suspend fun inbox(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Thing.Listing<Thing>>

    @GET("/message/unread.json")
    suspend fun unread(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Thing.Listing<Thing>>

    @GET("/message/sent.json")
    suspend fun sent(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Thing.Listing<Thing>>

    @GET("/message/mentions.json")
    suspend fun mentions(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Thing.Listing<Thing>>

    @POST("/api/read_all_messages")
    suspend fun readAll(): Response<Unit>

    @POST("/api/read_message")
    suspend fun markRead(@Query("id") id: String)

    @POST("/api/unread_message")
    suspend fun markUnread(@Query("id") id: String)

}