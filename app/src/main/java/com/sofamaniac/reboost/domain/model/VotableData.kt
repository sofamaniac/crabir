package com.sofamaniac.reboost.domain.model

interface VotableData {
    val id: String
    val name: String
    val relationship: Relationship
    val score: Score

    fun copy(relationship: Relationship? = null, score: Score? = null): VotableData
}