package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Thing.Post
import com.sofamaniac.crabir.data.remote.dto.Thing.Subreddit
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Tag

interface SubredditAPI {
    @GET("{sort}.json")
    suspend fun getHome(
        @Path("sort") sort: Sort,
        @Query("t") timeframe: Timeframe? = null,
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Listing<Post>>

    @GET("{subreddit}/about.json")
    suspend fun getSubInfo(
        @Path(
            "subreddit",
            encoded = true
        ) subreddit: String,
    ): Result<Subreddit>

    /** Get the list of subreddits the user is subscribed to. */
    @GET("/subreddits/mine/subscriber.json")
    suspend fun getSubreddits(
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
    ): Result<Listing<Subreddit>>

    /** Get the post of a given subreddit.
     *
     * @param subreddit The name of the subreddit without the `r/` prefix
     * */
    @GET("{subreddit}/{sort}.json")
    suspend fun getSubreddit(
        @Path("subreddit", encoded = true) subreddit: String,
        @Path("sort") sort: Sort = Sort.Best,
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("t") timeframe: Timeframe? = null,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Result<Listing<Post>>

    @FormUrlEncoded
    @POST("api/subscribe")
    suspend fun subscribe(
        @Field("action") action: SubscribeAction,
        @Field("sr") subreddit: Fullname,
    ): Result<Unit>

    @FormUrlEncoded
    @POST("api/subscribe")
    suspend fun subscribe(
        @Field("action") action: SubscribeAction,
        @Field("sr_name") subreddit: String,
    ): Result<Unit>

    @GET("{subreddit}/about/rules.json")
    suspend fun getRules(@Path("subreddit", encoded = true) subreddit: String): Result<Rules>

    @FormUrlEncoded
    @POST("api/favorite")
    suspend fun favorite(
        @Field("sr_name") name: String,
        @Field("make_favorite") favorite: Boolean,
    ): Result<Unit>

    @GET("api/crosspostable_subreddits.json")
    suspend fun getCrosspostableSubreddits(
        @Query("sr_detail") details: Boolean = true,
        @Query("after") after: Fullname? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Tag account: RedditAccount? = null,
    ): Result<Listing<Subreddit>>
}
