/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:52 PM
 *
 */

package com.sofamaniac.reboost.data.remote.api

import com.sofamaniac.reboost.data.remote.api.auth.RedditAuthApi
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.Thing.More
import com.sofamaniac.reboost.data.remote.dto.Thing.Post
import com.sofamaniac.reboost.data.remote.dto.Thing.Subreddit
import com.sofamaniac.reboost.data.remote.dto.post.PostId
import com.sofamaniac.reboost.data.remote.utils.CommentsResponseSerializer
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.sofamaniac.reboost.data.remote.dto.Timeframe as PostTimeframe
import com.sofamaniac.reboost.data.remote.dto.comment.Sort as CommentSort
import com.sofamaniac.reboost.data.remote.dto.post.Sort as PostSort

private const val BASE_URL = "https://oauth.reddit.com/"


internal const val API_LIMIT = 100

interface RedditAPIService : VotableAPI, PostAPI, RedditAuthApi, UserAPI {

    @GET("{sort}.json")
    suspend fun getHome(
        @Path("sort") sort: PostSort,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("after") after: String? = null,
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
        @Query("after") after: String? = null,
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
        @Query("after") after: String? = null,
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
        @Query("after") after: String? = null,
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
        @Query("limit") limit: Int? = null,
    ): Response<CommentsResponse>

    @GET("{permalink}.json")
    suspend fun getThread(
        @Path(value = "permalink", encoded = true) permalink: String,
        @Query("showedits") showEdits: Boolean = false,
        @Query("showmore") showMore: Boolean = false,
        @Query("showmedia") showMedia: Boolean = false,
        @Query("showtitle") showTitle: Boolean = false,
        @Query("sort") sort: CommentSort = CommentSort.Best,
        @Query("comment") comment: String? = null,
        @Query("context") context: Int? = 0,
        @Query("depth") depth: Int? = null,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<CommentsResponse>
}

@Serializable(with = CommentsResponseSerializer::class)
data class CommentsResponse(
    /** Contains only 1 (one) [Post] */
    val post: Listing<Post>,
    /** List of [com.sofamaniac.reboost.data.remote.dto.Comment] and [More] */
    val comments: Listing<Thing>,
    val more: More? = null,
)

