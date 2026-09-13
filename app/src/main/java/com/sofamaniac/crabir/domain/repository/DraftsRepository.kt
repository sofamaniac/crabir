package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.Draft
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.DraftAPI
import com.sofamaniac.crabir.data.remote.reddit.DraftSubmit
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class DraftsRepository(private val api: DraftAPI) {
    private val _drafts: MutableStateFlow<List<Draft>> = MutableStateFlow(emptyList())
    val drafts: StateFlow<List<Draft>> = _drafts

    private val _subreddits: MutableStateFlow<Map<Fullname, SubredditData>> =
        MutableStateFlow(emptyMap())
    val subreddits: StateFlow<Map<Fullname, SubredditData>> = _subreddits

    suspend fun getDrafts(): Unit {
        val draftResponse =
            api.drafts()
                .onFailure { e -> Log.e("DraftsRepository", "Error getting drafts: ", e) }
                .getOrNull()
        if (draftResponse != null) {
            _drafts.value = draftResponse.drafts
            _subreddits.value =
                draftResponse.subreddits.map { SubredditDTOMapper.map(it) }.associateBy { it.name }
        }
    }

    suspend fun updateDraft(
        draft: DraftSubmit,
        account: RedditAccount?,
    ): Result<Unit> {
        if (!draft.check()) return Result.failure(InvalidDraftSubmit())
        return api.updateDraft(draft.build(), account)
    }

    suspend fun createDraft(draft: DraftSubmit, account: RedditAccount?): Result<Unit> {
        if (!draft.check()) return Result.failure(InvalidDraftSubmit())
        return api.createDraft(draft.build(), account)
    }

    suspend fun deleteDraft(draftId: String): Result<Unit> {
        return api.deleteDraft(draftId)
            .onSuccess {
                _drafts.update { drafts ->
                    drafts.filter { it.id != draftId }
                }
            }
    }
}

class InvalidDraftSubmit : Error("Invalid draft submission")
