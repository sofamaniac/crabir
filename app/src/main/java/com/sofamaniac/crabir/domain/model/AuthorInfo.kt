package com.sofamaniac.crabir.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthorInfo(
    val username: String,
    val flair: Flair,
    val authorFullname: String = "",
    val isAuthorBlocked: Boolean = false,
    val hasPatreonFlair: Boolean = false,
    val isAuthorPremium: Boolean = false,
) {
    companion object {
        val DUMMY = AuthorInfo(
            username = "user123",
            flair = EMPTY_FLAIR,
            authorFullname = "t2_dummy",
            isAuthorBlocked = false,
            hasPatreonFlair = false,
            isAuthorPremium = false,
        )
    }
}