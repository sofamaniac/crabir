package com.sofamaniac.crabir.ui.editor.crosspostEditor

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sofamaniac.crabir.data.remote.reddit.CrosspostSubmissionBuilder
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubmissionBuilderError
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.CrosspostCommunitiesRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.ListingSource
import com.sofamaniac.crabir.ui.editor.postEditor.CreatorViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CrosspostCreatorViewModel(
    @InjectedParam val parentFullname: Fullname,
    api: RedditAPIService,
    private val communities: CrosspostCommunitiesRepository,
    linksRepository: LinksRepository,
    accountsRepository: AccountsRepository,
) : CreatorViewModel(api) {
    var state by mutableStateOf(
        CrosspostSubmissionBuilder(
            crosspostFullname = parentFullname
        )
    )
    val post = linksRepository.get(parentFullname)

    var loading by mutableStateOf(false)
    val accounts: Flow<List<RedditAccount>> = accountsRepository.accounts
    private var account: RedditAccount? = null

    var source = CrosspostCommunitiesSource(communities, params = account)

    val data: Flow<PagingData<SubredditData>> = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = Fullname(""),
        pagingSourceFactory = {
            CrosspostCommunitiesSource(
                communities,
                account,
            ).also { source = it }
        }
    )
        .flow.cachedIn(
            viewModelScope
        )

    fun setAccount(account: RedditAccount?) {
        this.account = account
        source.invalidate()
    }

    suspend fun submit(account: RedditAccount?): Result<Unit> {
        loading = true
        state = state.copy(
            title = titleState.text as String,
            subreddit = community?.displayName ?: ""
        )
        val submission = state.build()
        if (submission.isFailure) {
            Log.e("PostCreatorViewModel", "submit: ${submission.exceptionOrNull()}")
            error = submission.exceptionOrNull() as SubmissionBuilderError?
            loading = false
            return Result.failure(error!!)
        } else {
            val res = api.submitPost(submission.getOrThrow(), account = account)
            loading = false
            return if (res.isSuccess) {
                val response = res.getOrNull()
                Log.d("PostCreatorViewModel", "submit: $response")
                if (response?.json?.errors?.isNotEmpty() == true) {
                    Result.failure(Exception(response.json.errors.toString()))
                } else {
                    Result.success(Unit)
                }
            } else {
                Result.failure(Exception("Failed to submit post: ${res.exceptionOrNull()}"))
            }
        }
    }
}

typealias CrosspostCommunitiesSource = ListingSource<RedditAccount?, SubredditData>