package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.dto.MoreData
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Instant

@Serializable
sealed class CommentType() {
    data class Comment(val comment: CommentData) : CommentType()
    data class More(val data: MoreData) : CommentType()

    val name: Fullname
        get() =
            when (this) {
                is Comment -> comment.name
                is More -> data.name
            }

    val parentId: Fullname
        get() =
            when (this) {
                is Comment -> comment.parentId
                is More -> data.parentId
            }


    val depth: Int
        get() =
            when (this) {
                is Comment -> comment.depth
                is More -> data.depth
            }
}

@Serializable
data class CommentData(
    override val id: String,
    override val name: Fullname,
    val depth: Int,
    val bodyMd: String,
    val bodyHtml: String,
    val parentId: Fullname,
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
    val mediaMetadata: Map<String, MediaMetadata>,
    val distinguished: String? = ""
) : VotableData {
    override fun copy(relationship: Relationship?, score: Score?): CommentData =
        copy(relationship = relationship ?: this.relationship, score = score ?: this.score)

    override fun updateScore(oldLikes: Boolean?, newLikes: Boolean?): CommentData {
        return super.updateScore(oldLikes, newLikes) as CommentData
    }

    fun updateReplies(replies: List<CommentType>): CommentData = copy(replies = replies)

    override fun toEntity(): VotableEntity {
        return VotableEntity(
            id = name,
            data = Json.encodeToString(this)
        )
    }
}
