package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.local.entities.asVotableData
import com.sofamaniac.crabir.domain.repository.Cache
import com.sofamaniac.crabir.domain.repository.DataInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface VotableData : DataInterface {
    val author: AuthorInfo?
    val relationship: Relationship
    val score: Score
    val body: ParsedMarkdown
    fun copy(relationship: Relationship? = null, score: Score? = null): VotableData
    fun toEntity(): VotableEntity

    fun updateScore(oldLikes: Boolean?, newLikes: Boolean?): VotableData {
        if (oldLikes == newLikes) {
            return this
        }
        val ups = score.ups + when {
            newLikes == true -> 1
            oldLikes == true -> -1
            else -> 0
        }
        val downs = score.downs + when {
            newLikes == false -> 1
            oldLikes == false -> -1
            else -> 0
        }
        val newScore = score.copy(
            ups = ups,
            downs = downs, score = ups - downs,
            upvoteRatio = ups / (ups + downs).toDouble().coerceAtMost(1.0)
        )
        return copy(score = newScore)
    }

}

class VotableCache(private val dao: VotableDao) : Cache<VotableData> {
    override suspend fun insert(thing: VotableData) {
        dao.insert(thing.toEntity())
    }

    override suspend fun insert(things: List<VotableData>) {
        dao.insert(things.map { it.toEntity() })
    }

    override suspend fun update(thing: VotableData) {
        dao.update(thing.name, thing.toEntity().data)
    }

    override fun get(name: Fullname): Flow<VotableData?> {
        return dao.get(name).map { it?.asVotableData() }
    }

    override suspend fun getAsync(name: Fullname): VotableData? {
        return dao.getValue(name)?.asVotableData()
    }

    override suspend fun delete(name: Fullname) {
        dao.delete(name)
    }

    override suspend fun clear() {
        dao.clear()
    }
}
