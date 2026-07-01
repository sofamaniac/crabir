package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.VotableData
import org.koin.core.annotation.Singleton

@Singleton
class CommentsRepository(
    override val api: RedditAPIService,
    override val votableDao: VotableDao
) : VotableRepository<CommentType>

@Singleton
class MixedRepository(
    override val api: RedditAPIService,
    override val votableDao: VotableDao
) : VotableRepository<VotableData>
