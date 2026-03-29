package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.domain.repository.DataInterface

interface VotableData : DataInterface {
    val relationship: Relationship
    val score: Score
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
            upvoteRatio = ups / (ups + downs).toDouble()
        )
        return copy(score = newScore)
    }

}