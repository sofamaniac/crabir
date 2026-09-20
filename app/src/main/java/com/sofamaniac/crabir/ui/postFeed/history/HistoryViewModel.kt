package com.sofamaniac.crabir.ui.postFeed.history

import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.HistoryRepository
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HistoryViewModel(
    repository: HistoryRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditDao,
    @InjectedParam initialSort: Sort,
    @InjectedParam initialTimeframe: Timeframe?,
) : PostFeedViewModel<SubredditData>(
    repository,
    visitedPostsDao,
    communityDao,
    initialSort,
    initialTimeframe
)
