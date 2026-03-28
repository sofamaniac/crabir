package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.domain.repository.DataInterface

interface VotableData : DataInterface {
    val relationship: Relationship
    val score: Score
    fun copy(relationship: Relationship? = null, score: Score? = null): VotableData
    fun toEntity(): VotableEntity

}