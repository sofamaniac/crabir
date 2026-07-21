package com.sofamaniac.crabir.domain.repository.feed

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.LinksRepository
import org.koin.core.annotation.ViewModelScope

@ViewModelScope
class MultiPostsRepository(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<FeedParams>() {
    private var currentMulti: String? = null

    fun updateMulti(permalink: String) {
        currentMulti = permalink
    }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        val subreddit = currentMulti ?: return PagingSource.LoadResult.Page(emptyList(), null, null)
        return makeRequest {
            api.getMultireddit(
                path = subreddit,
                after = after,
                sort = params.sort,
                timeframe = params.timeframe
            )
        }
    }
}