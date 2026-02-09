/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.api

import com.sofamaniac.reboost.data.remote.dto.post.PostFullname
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface PostAPI {

    @POST("/api/hide")
    suspend fun hide(@Query("id") postFullname: PostFullname): Response<Unit>

    @POST("/api/unhide")
    suspend fun unhide(@Query("id") postFullname: PostFullname): Response<Unit>
}
