package com.sofamaniac.reboost.domain.model

import kotlin.time.Instant

sealed class CommentReply() {
    data class Comment(val comment: CommentData) : CommentReply()
    data class More(val more: String) : CommentReply()
}

data class CommentData(
    override val id: String,
    override val name: String,
    val depth: Int,
    val bodyMd: String,
    val bodyHtml: String,
    val parentId: String,
    val permalink: String,
    val replies: List<CommentReply>,
    val author: AuthorInfo,
    override val relationship: Relationship,
    val subredditInfo: SubredditInfo,
    override val score: Score,
    val collapsed: Boolean,
    val createdUtc: Instant,
    val edited: Instant?,
) : VotableData {
    override fun copy(relationship: Relationship?, score: Score?): CommentData =
        copy(relationship = relationship ?: this.relationship, score = score ?: this.score)
}
