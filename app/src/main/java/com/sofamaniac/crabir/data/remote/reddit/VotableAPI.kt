/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.domain.model.Fullname
import retrofit2.http.POST
import retrofit2.http.Query


interface VotableAPI {
    /**
     * Votes on a post.
     *
     * Allows a user to cast a vote on a specific post.
     *
     * @param fullname The full name of the post / comment to vote on.
     * @param dir The direction of the vote.
     *                  -  `1`: Upvote
     *                  - `-1`: Downvote
     *                  -  `0`: Neutral/Clear Vote (removes any existing vote)
     * @throws Throwable if any other error occurs during the request.
     *
     * See [POST /api/vote](https://www.reddit.com/dev/api#POST_api_vote) for more information.
     */
    @POST("/api/vote")
    suspend fun vote(@Query("id") fullname: Fullname, @Query("dir") dir: Int): Result<Unit>

    @POST("/api/save")
    suspend fun save(@Query("id") fullname: Fullname): Result<Unit>

    @POST("/api/unsave")
    suspend fun unsave(@Query("id") fullname: Fullname): Result<Unit>
}

const val UPVOTED = 1
const val DOWNVOTED = -1
const val NEUTRAL = 0
