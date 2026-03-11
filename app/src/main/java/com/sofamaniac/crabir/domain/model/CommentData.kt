package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.remote.dto.MoreData
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import kotlin.time.Instant

sealed class CommentType() {
    data class Comment(val comment: CommentData) : CommentType()
    data class More(val data: MoreData) : CommentType()

    val name: String
        get() =
            when (this) {
                is Comment -> comment.name
                is More -> data.name
            }

    val depth: Int
        get() =
            when (this) {
                is Comment -> comment.depth
                is More -> data.depth
            }
}

data class CommentData(
    override val id: String,
    override val name: String,
    val depth: Int,
    val bodyMd: String,
    val bodyHtml: String,
    val parentId: String,
    val permalink: String,
    val replies: List<CommentType>,
    val author: AuthorInfo,
    val isSubmitter: Boolean,
    override val relationship: Relationship,
    val subredditInfo: SubredditInfo,
    override val score: Score,
    val collapsed: Boolean,
    val createdUtc: Instant,
    val edited: Instant?,
    val mediaMetadata: Map<String, MediaMetadata>
) : VotableData {
    override fun copy(relationship: Relationship?, score: Score?): CommentData =
        copy(relationship = relationship ?: this.relationship, score = score ?: this.score)

    fun updateReplies(replies: List<CommentType>): CommentData = copy(replies = replies)
}
