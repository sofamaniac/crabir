package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Draft
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTO
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import kotlinx.serialization.Serializable
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Tag

interface DraftAPI {

    @GET("api/v1/drafts.json")
    suspend fun drafts(): Result<DraftsResponse>

    @POST("api/v1/draft")
    @FormUrlEncoded
    suspend fun createDraft(
        @FieldMap draft: Map<String, String>,
        @Tag account: RedditAccount?,
    ): Result<Unit>

    @PUT("api/v1/draft")
    @FormUrlEncoded
    suspend fun updateDraft(
        @FieldMap draft: Map<String, String>,
        @Tag account: RedditAccount?,
    ): Result<Unit>

    @DELETE("api/v1/draft")
    suspend fun deleteDraft(@Field("draft_id") id: String): Result<Unit>
}

@Serializable
data class DraftsResponse(
    val subreddits: List<SubredditDTO>,
    val drafts: List<Draft>,
)

data class DraftSubmit(
    val id: String? = null,
    val title: String? = null,
    val url: String? = null,
    val text: String? = null,
    val subreddit: SubredditData? = null,
    val nsfw: Boolean = false,
    val spoiler: Boolean = false,
    val flairId: String? = null,
    val flairText: String? = null,
    val sendReplies: Boolean = false,
    val originalContent: Boolean = false,
) {
    fun check(): Boolean {
        val emptyTitle = title.isNullOrBlank() && (url != null || text != null)
        return id != null || emptyTitle || title != null
    }


    fun build(): Map<String, String> {
        return buildMap {
            id?.let { put("id", it) }
            title?.let { put("title", it) }
            url?.let {
                put("body", url)
                put("kind", "link")
                if (text != null) {
                    put("optional_text", text)
                }
            }
            text?.let {
                if (url == null) {
                    put("body", text)
                    put("kind", "markdown")
                }
            }
            put("send_replies", sendReplies.toString())
            put("nsfw", nsfw.toString())
            put("spoiler", spoiler.toString())
            put("original_content", originalContent.toString())
            flairId?.let { put("flair_id", it) }
            flairText?.let { put("flair_text", it) }
            subreddit?.let {
                put("subreddit", it.name.name)
                val target = if (subreddit.displayName.startsWith("u_")) {
                    "profile"
                } else {
                    "subreddit"
                }
                put("target", target)
            }
        }
    }
}
