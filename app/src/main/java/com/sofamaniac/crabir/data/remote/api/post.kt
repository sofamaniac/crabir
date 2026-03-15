/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.api

import com.sofamaniac.crabir.data.remote.dto.post.PostFullname
import com.sofamaniac.crabir.domain.model.Kind
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface PostAPI {

    @POST("/api/hide")
    suspend fun hide(@Query("id") postFullname: PostFullname): Response<Unit>

    @POST("/api/unhide")
    suspend fun unhide(@Query("id") postFullname: PostFullname): Response<Unit>

    @FormUrlEncoded
    @POST("api/submit")
    suspend fun submitPost(@FieldMap post: Map<String, String>): Response<Unit>
}

class PostSubmissionBuilder() {
    var title: String = ""
    var text: String? = null
    var subreddit: String = ""
    var nsfw: Boolean = false
    var spoiler: Boolean = false
    var sendReplies: Boolean = false
    var flairId: String? = null
    var flairText: String? = null
    var url: String? = null
    var kind: Kind = Kind.Self

    fun build(): Result<Map<String, String>> {
        if (title.isBlank()) return Result.failure(Exception("Title cannot be empty"))
        else if (subreddit.isBlank()) return Result.failure(Exception("Subreddit cannot be empty"))
        else if (text.isNullOrBlank() && url.isNullOrBlank()) return Result.failure(Exception("Text or url must not be empty"))
        else if (kind == Kind.Link && url.isNullOrBlank()) return Result.failure(Exception("Url must not be empty"))

        return Result.success(
            buildMap {
                put("api_type", "json")
                put("kind", kind.toApiString())
                put("title", title)
                put("sr", subreddit)
                put("sendReplies", sendReplies.toString())
                put("nsfw", nsfw.toString())
                put("spoiler", spoiler.toString())
                text?.let { put("text", it) }
                url?.let { put("url", it) }
                flairId?.let { put("flair_id", it) }
                flairText?.let { put("flair_text", it) }
            }
        )
    }
}

internal fun Kind.toApiString(): String {
    return when (this) {
        Kind.Self -> "self"
        Kind.Image -> "image"
        Kind.Video -> "video"
        Kind.Link -> "link"
        else -> "link"
    }
}