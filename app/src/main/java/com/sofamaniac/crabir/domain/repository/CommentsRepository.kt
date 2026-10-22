package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.domain.model.votable.VotableData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.ViewModelScope


@ViewModelScope
class CommentsRepository(
    override val api: RedditAPIService,
    override val cache: CommentsCache,
) : VotableRepository<CommentType> {
    fun getMany(commentsNames: Iterable<Fullname>): Flow<List<CommentType>> {
        return cache.getMany(commentsNames)
    }
}

interface CommentsCache : Cache<CommentType> {
    fun getMany(names: Iterable<Fullname>): Flow<List<CommentType>>
}

@ViewModelScope
class CommentsCacheImpl : CommentsCache {
    private val cache: MutableStateFlow<Map<Fullname, CommentType>> = MutableStateFlow(emptyMap())
    override suspend fun insert(thing: CommentType) {
        cache.value += (thing.name to thing)
    }

    override suspend fun insert(things: List<CommentType>) {
        cache.value += things.associateBy { it.name }
    }

    override suspend fun update(thing: CommentType) {
        insert(thing)
    }

    override fun get(name: Fullname): Flow<CommentType?> {
        return cache.map { it[name] }.distinctUntilChanged()
    }

    override suspend fun getAsync(name: Fullname): CommentType? {
        return cache.value[name]
    }

    override suspend fun delete(name: Fullname) {
        cache.value -= name
    }

    override suspend fun clear() {
        cache.value = emptyMap()
    }

    override fun getMany(names: Iterable<Fullname>): Flow<List<CommentType>> {
        return cache.map { cache -> names.mapNotNull { name -> cache[name] } }
    }

}

@Singleton
class MixedRepository(
    override val api: RedditAPIService,
    @Qualifier(VotableData::class) override val cache: Cache<VotableData>,
) : VotableRepository<VotableData>

@Singleton(binds = [Cache::class])
@Qualifier(VotableData::class)
class MixedCache(override val cache: VotableDao) : VotableCache<VotableData>() {
    override fun VotableEntity?.transform(): VotableData? {
        return this?.asVotableData()
    }
}
