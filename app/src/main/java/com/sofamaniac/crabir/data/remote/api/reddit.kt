/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:52 PM
 *
 */

package com.sofamaniac.crabir.data.remote.api

import com.sofamaniac.crabir.data.remote.api.auth.RedditAuthApi
import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Thing.More
import com.sofamaniac.crabir.data.remote.dto.Thing.Post
import com.sofamaniac.crabir.data.remote.dto.Thing.Subreddit
import com.sofamaniac.crabir.data.remote.dto.post.PostId
import com.sofamaniac.crabir.data.remote.utils.CommentsResponseSerializer
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.sofamaniac.crabir.data.remote.dto.Timeframe as PostTimeframe
import com.sofamaniac.crabir.data.remote.dto.comment.Sort as CommentSort
import com.sofamaniac.crabir.data.remote.dto.post.Sort as PostSort

private const val BASE_URL = "https://oauth.reddit.com/"


internal const val API_LIMIT = 100

interface RedditAPIService : VotableAPI, PostAPI, RedditAuthApi, UserAPI, SearchAPI {

    @GET("{sort}.json")
    suspend fun getHome(
        @Path("sort") sort: PostSort,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Post>>

    @GET("/r/{subreddit}/about.json")
    suspend fun getSubInfo(@Path("subreddit") subreddit: String): Response<Subreddit>

    @GET("user/{username}/about.json")
    suspend fun getUserAbout(@Path("username") username: String): Response<Listing<Post>>

    /** Get the list of subreddits the user is subscribed to. */
    @GET("/subreddits/mine/subscriber")
    suspend fun getSubreddits(
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<Listing<Subreddit>>

    /** Get the post of a given subreddit.
     *
     * @param subreddit The name of the subreddit without the `r/` prefix
     * */
    @GET("/r/{subreddit}/{sort}.json")
    suspend fun getSubreddit(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: PostSort = PostSort.Best,
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Post>>

    /** Get the list of multis the user is subscribed to. */
    @GET("/api/multi/mine.json?raw_json=1")
    suspend fun getMultireddits(
        @Query("expand_srs") expandSrs: Boolean = true,
    ): Response<List<Thing.Multi>>

    /** Get the post of a given multi.
     *
     * @param path The permalink of the multi
     * */
    @GET("{path}/{sort}.json")
    suspend fun getMultreddit(
        @Path("path", encoded = true) path: String,
        @Path("sort") sort: PostSort = PostSort.Best,
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Post>>

    /**
     * Gets the comments for a post.
     *
     * @param subreddit The name of the subreddit where the post is located.
     * @param id The ID of the post.
     * @param comment focal point of the returned view
     * @param context Number of parents to be shown when [comment] is set
     * @param depth is the maximum depth of subtrees in the thread
     * @param showMore whether to show [More] or not
     */
    @GET("/r/{subreddit}/comments/{id}.json")
    suspend fun getComments(
        @Path("subreddit") subreddit: String,
        @Path("id") id: PostId,
        @Query("showedits") showEdits: Boolean = true,
        @Query("showmore") showMore: Boolean = true,
        @Query("showmedia") showMedia: Boolean = true,
        @Query("showtitle") showTitle: Boolean = true,
        @Query("sort") sort: CommentSort? = null,
        @Query("comment") comment: String? = null,
        @Query("context") context: Int? = null,
        @Query("depth") depth: Int? = null,
        @Query("limit") limit: Int? = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<CommentsResponse>

    /**
     * Gets the post and comments for a post.
     *
     * @param subreddit The name of the subreddit where the post is located.
     * @param id The ID of the post.
     * @param comment focal point of the returned view
     * @param context Number of parents to be shown when [comment] is set
     * @param depth is the maximum depth of subtrees in the thread
     * @param showMore whether to show [More] or not
     */
    @GET("{permalink}.json")
    suspend fun getThread(
        @Path(value = "permalink", encoded = true) permalink: String,
        @Query("showedits") showEdits: Boolean = true,
        @Query("showmore") showMore: Boolean = true,
        @Query("showmedia") showMedia: Boolean = true,
        @Query("showtitle") showTitle: Boolean = true,
        @Query("sort") sort: CommentSort = CommentSort.Best,
        @Query("comment") comment: String? = null,
        @Query("context") context: Int? = 0,
        @Query("depth") depth: Int? = null,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<CommentsResponse>

    /** Get comments for a [Thing.More]
     *
     * @param parentId The id of the parent comment
     * @param children A comma separated list of comments id to get. Max 100.
     */
    @GET("api/morechildren.json")
    suspend fun getMoreComments(
        @Query("link_id") parentId: Fullname,
        @Query("children") children: String,
        @Query("api_type") apiType: String = "json",
        @Query("sort") sort: CommentSort? = null,
    ): Response<MoreResponseOuter>

    @POST("api/comment")
    suspend fun postComment(
        @Body body: RequestBody
    ): Response<MoreResponseOuter>

    @GET("r/{subreddit}/about/rules.json")
    suspend fun getRules(@Path("subreddit") subreddit: String): Response<Rules>

    @GET("r/{subreddit}/api/link_flair_v2.json")
    suspend fun getPostFlair(@Path("subreddit") subreddit: String): Response<List<FlairInfo>>

    @FormUrlEncoded
    @POST("api/report")
    suspend fun report(
        @Field("thing_id") id: Fullname,
        @Field("reason") reason: String,
        @Field("api_type") apiType: String = "json"
    ): Response<Unit>

    @FormUrlEncoded
    @POST("api/subscribe")
    suspend fun subscribe(
        @Field("action") action: SubscribeAction,
        @Field("sr") subreddit: String,
    ): Response<Unit>

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



fun postCommentBody(parentId: Fullname, text: String): RequestBody {
    return MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("api_type", "json")
        .addFormDataPart("text", text)
        .addFormDataPart("thing_id", parentId.name)
        .addFormDataPart("raw_json", "1")
        .build()
}

@Serializable(with = CommentsResponseSerializer::class)
data class CommentsResponse(
    /** Contains only 1 (one) [Post] */
    val post: Listing<Post>,
    /** List of [Thing.Comment] and [More] */
    val comments: Listing<Thing>,
)

@Serializable
data class MoreResponseOuter(
    val json: MoreResponse
)

@Serializable
data class MoreResponse(
    val data: MoreResponseData
)

@Serializable
data class MoreResponseData(
    val things: List<Thing>
)

@Serializable
data class Rules(
    val rules: List<Rule> = emptyList(),
    @SerialName("site_rules") val siteRules: List<String> = emptyList()
)

@Serializable
data class Rule(
    val kind: String,
    val description: String,
    @SerialName("short_name") val shortName: String,
    @SerialName("violation_reason") val violationReason: String,
    val priority: Int,
)

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