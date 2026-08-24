package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Thing.More
import com.sofamaniac.crabir.data.remote.dto.Thing.Post
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.PostId
import com.sofamaniac.crabir.data.remote.utils.CommentsResponseSerializer
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Tag

interface ThreadAPI {
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
        @Query("sort") sort: Sort? = null,
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
        @Query("sort") sort: Sort? = null,
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
        @Query("sort") sort: Sort? = null,
    ): Response<MoreResponseOuter>

    @POST("api/comment")
    suspend fun submitComment(
        @Body body: RequestBody,
        @Tag account: RedditAccount?,
    ): Response<MoreResponseOuter>
}

fun commentSubmissionBody(parentId: Fullname, text: String): RequestBody {
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
    val json: MoreResult
)

@Serializable
data class MoreResult(val data: MoreResponseData? = null, val errors: List<List<String>>? = null)

@Serializable
data class MoreResponseData(
    val things: List<Thing>
)
