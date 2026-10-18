package com.sofamaniac.crabir.domain.model.votable

import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    val clicked: Boolean,
    val visited: Boolean,
    val saved: Boolean,
    val liked: Boolean?,
    val hidden: Boolean,
)
