/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:52 PM
 *
 */

package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Thing.Post
import com.sofamaniac.crabir.data.remote.reddit.auth.RedditAuthApi
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.sofamaniac.crabir.data.remote.dto.Timeframe as PostTimeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort as PostSort

internal const val API_LIMIT = 100

const val HOME = "_HOME"
const val HISTORY = "_HISTORY"

interface RedditAPIService :
    VotableAPI,
    PostAPI,
    SubredditAPI,
    RedditAuthApi,
    UserAPI,
    SearchAPI,
    ThreadAPI,
    InboxAPI,
    DraftAPI {


    /** Get the list of multis the user is subscribed to. */
    @GET("/api/multi/mine.json?raw_json=1")
    suspend fun getMultireddits(
        @Query("expand_srs") expandSrs: Boolean = true,
    ): Result<List<Thing.Multi>>

    /** Get the post of a given multi.
     *
     * @param path The permalink of the multi
     * */
    @GET("{path}/{sort}.json")
    suspend fun getMultireddit(
        @Path("path", encoded = true) path: String,
        @Path("sort") sort: PostSort = PostSort.Best,
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Listing<Post>>


    /** Get the list of flair for a given subreddit.
     * @param subreddit The subreddit to get the flair for. Must be prefixed with 'r/'
     */
    @GET("{subreddit}/api/link_flair_v2.json")
    suspend fun getPostFlair(
        @Path(
            "subreddit",
            encoded = true
        ) subreddit: String,
    ): Result<List<FlairInfo>>

    @FormUrlEncoded
    @POST("api/report")
    suspend fun report(
        @Field("thing_id") id: Fullname,
        @Field("reason") reason: String,
        @Field("api_type") apiType: String = "json",
    ): Result<Unit>
}

enum class SubscribeAction {
    SUBSCRIBE,
    UNSUBSCRIBE;

    override fun toString(): String {
        return when (this) {
            SUBSCRIBE -> "sub"
            UNSUBSCRIBE -> "unsub"
        }
    }
}


@Serializable
data class FlairInfo(
    val text: String,
    val richtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("text_editable") val textEditable: Boolean = false,
    val type: String = "text",
    val id: String,
    @SerialName("background_color") val backgroundColor: String = "transparent",
    @SerialName("text_color") val textColor: String? = null,
    @SerialName("max_emojis") val maxEmojis: Int? = null,
)

@Serializable
data class RedditResponse<T>(
    val json: RedditResponseInner<T>,
) {
    fun toResult(): Result<T> {
        return if (json.data != null) {
            Result.success(json.data)
        } else {
            Result.failure(Exception(json.errors.joinToString()))
        }
    }
}

@Serializable
data class RedditResponseInner<T>(
    val data: T?,
    val errors: List<List<String>>,
)
