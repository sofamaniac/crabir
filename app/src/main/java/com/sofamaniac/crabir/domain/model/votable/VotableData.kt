package com.sofamaniac.crabir.domain.model.votable

import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.domain.model.AuthorInfo
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import com.sofamaniac.crabir.domain.repository.DataInterface

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
        val total = (ups + downs).coerceAtLeast(1).toDouble()
        val newScore = score.copy(
            ups = ups,
            downs = downs, score = ups - downs,
            upvoteRatio = (ups / total).coerceAtMost(1.0)
        )
        return copy(score = newScore)
    }
}
