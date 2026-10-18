package com.sofamaniac.crabir.domain.model.votable.post

import kotlinx.serialization.Serializable

enum class RemovalKind {
    Reddit,
    Moderator
}

@Serializable
data class RemovalInfo(
    val removedBy: String? = null,
    val removedByCategory: String? = null,
    val removalReason: String? = null,
) {
    val kind: RemovalKind? = when (removedByCategory) {
        "reddit" -> RemovalKind.Reddit
        "moderator" -> RemovalKind.Moderator
        else -> null
    }
}
