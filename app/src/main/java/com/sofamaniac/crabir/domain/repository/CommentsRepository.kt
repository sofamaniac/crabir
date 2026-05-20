package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.VotableData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsRepository @Inject constructor(
    override val api: RedditAPIService,
    override val votableDao: VotableDao
) : VotableRepository<CommentData>

@Singleton
class MixedRepository @Inject constructor(
    override val api: RedditAPIService,
    override val votableDao: VotableDao
) : VotableRepository<VotableData>
