package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.data.local.entities.into
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
class CommentsRepository(
    override val api: RedditAPIService,
    override val votableDao: VotableDao,
) : VotableRepository<CommentType> {
    override fun VotableEntity?.transform(): CommentType? {
        return this?.into<CommentType>()
    }

    suspend fun insertAsync(things: List<CommentType>) {
        votableDao.insert(things.map { it.toEntity() })
    }

    fun getMany(commentsNames: Iterable<Fullname>): Flow<List<CommentType>> {
        return votableDao.getMany(commentsNames.toList()).map { list ->
            list.mapNotNull { it.transform() }
        }
    }
}

@Singleton
class MixedRepository(
    override val api: RedditAPIService,
    override val votableDao: VotableDao,
) : VotableRepository<VotableData> {
    override fun VotableEntity?.transform(): VotableData? {
        return this?.asVotableData()
    }
}
