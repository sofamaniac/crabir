package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.VotableData
import org.koin.core.annotation.Singleton

@Singleton
class CommentsRepository(
    override val api: RedditAPIService,
    override val votableDao: VotableDao,
) : VotableRepository<CommentType> {
    override fun VotableEntity?.into(): CommentType? {
        return this?.asVotableData() as? CommentType
    }
}

@Singleton
class MixedRepository(
    override val api: RedditAPIService,
    override val votableDao: VotableDao,
) : VotableRepository<VotableData> {
    override fun VotableEntity?.into(): VotableData? {
        return this?.asVotableData()
    }
}
