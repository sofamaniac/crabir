package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Tag

interface InboxAPI {
    @GET("/message/inbox.json")
    suspend fun inbox(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Thing.Listing<Thing>>

    @GET("/message/unread.json")
    suspend fun unread(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Thing.Listing<Thing>>

    @GET("/message/sent.json")
    suspend fun sent(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Thing.Listing<Thing>>

    @GET("/message/mentions.json")
    suspend fun mentions(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Thing.Listing<Thing>>

    @POST("/api/read_all_messages")
    suspend fun readAll(): Result<Unit>

    @POST("/api/read_message")
    suspend fun markRead(@Query("id") id: String): Result<Unit>

    @POST("/api/unread_message")
    suspend fun markUnread(@Query("id") id: String): Result<Unit>

    @FormUrlEncoded
    @POST("/api/compose")
    suspend fun compose(
        @Field("to") to: String,
        @Field("subject") subject: String,
        @Field("text") body: String,
        @Field("api_type") apiType: String = "json",
        @Tag account: RedditAccount? = null,
    ): Result<PostResponse>

    @FormUrlEncoded
    @POST("/api/comment")
    suspend fun reply(
        @Field("text") body: String,
        @Field("thing_id") parent: Fullname,
        @Field("return_rtjson") returnRtjson: Boolean = true,
        @Field("api_type") apiType: String = "json",
        @Tag account: RedditAccount? = null,
    ): Result<PostResponse>

    @POST("/api/del_msg")
    suspend fun delete(@Query("id") id: String): Result<Unit>

}

@Serializable
data class ComposeBody(
    @SerialName("subject") val subject: String,
    @SerialName("to") val to: String,
    @SerialName("text") val body: String,
    @SerialName("api_type") val apiType: String = "json",
)
