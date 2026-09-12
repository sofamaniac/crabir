package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Draft
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTO
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.POST

interface DraftAPI {

    @GET("api/v1/drafts.json")
    suspend fun drafts(): Result<DraftsResponse>

    @POST("api/v1/draft")
    suspend fun createDraft(): Result<Unit>
}

@Serializable
data class DraftsResponse(
    val subreddits: List<SubredditDTO>,
    val drafts: List<Draft>,
)
