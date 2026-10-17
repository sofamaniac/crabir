package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.dto.MoreData
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Instant

enum class CommentState {
    Visible,
    Collapsed,
    Hidden,
}

@Serializable
sealed class CommentType : VotableData {

    abstract val depth: Int
    abstract val parent: Fullname?
    abstract val descendants: Int
    abstract val state: CommentState
    abstract val previousSibling: Fullname?
    abstract val nextSibling: Fullname?

    abstract fun copy(
        depth: Int = this.depth,
        state: CommentState = this.state,
        previousSibling: Fullname? = this.previousSibling,
        nextSibling: Fullname? = this.nextSibling,
    ): CommentType

    override fun toEntity(): VotableEntity {
        return VotableEntity(id = name, data = Json.encodeToString(this))
    }

    @Serializable
    data class Comment(
        val comment: CommentData,
        override val depth: Int = comment.depth,
        override val descendants: Int = 0,
        override val state: CommentState = CommentState.Visible,
        override val previousSibling: Fullname? = null,
        override val nextSibling: Fullname? = null,
    ) : CommentType() {
        override fun copy(
            relationship: Relationship?,
            score: Score?,
        ): VotableData {
            return copy(comment = comment.copy(relationship = relationship, score = score))
        }

        override fun copy(
            depth: Int,
            state: CommentState,
            previousSibling: Fullname?,
            nextSibling: Fullname?,
        ): CommentType {
            return copy(
                comment = comment,
                depth = depth,
                state = state,
                previousSibling = previousSibling,
                nextSibling = nextSibling
            )
        }

        override val author: AuthorInfo = comment.author
        override val relationship: Relationship = comment.relationship
        override val score: Score = comment.score
        override val body: ParsedMarkdown = comment.body

        override val id: String = comment.id
        override val name: Fullname = comment.name
        override val parent: Fullname = comment.parentInfo.name
    }

    @Serializable
    data class More(
        val data: MoreData,
        override val depth: Int = data.depth,
        override val state: CommentState = CommentState.Visible,
        override val previousSibling: Fullname? = null,
        override val nextSibling: Fullname? = null,
    ) : CommentType() {
        override fun copy(
            relationship: Relationship?,
            score: Score?,
        ): VotableData {
            return this
        }

        override fun copy(
            depth: Int,
            state: CommentState,
            previousSibling: Fullname?,
            nextSibling: Fullname?,
        ): CommentType {
            return copy(
                data = data,
                depth = depth,
                state = state,
                previousSibling = previousSibling,
                nextSibling = nextSibling
            )
        }

        override val author: AuthorInfo? = null
        override val relationship: Relationship get() = throw IllegalAccessError("More has no relationship")
        override val score: Score get() = throw IllegalAccessError("More has no score")
        override val body: ParsedMarkdown get() = throw IllegalAccessError("More has no body")

        override val name: Fullname = data.name
        override val id: String = name.name
        override val parent: Fullname = data.parentId
        override val descendants: Int = data.children.size
    }
}

@Serializable
data class CommentData(
    override val id: String,
    override val name: Fullname,
    val depth: Int,
    val bodyMd: ParsedMarkdown,
    val bodyHtml: String,
    val richtext: RichtextDocument = RichtextDocument(emptyList()),
    val parentInfo: ParentInfo,
    val permalink: String,
    val replies: Int,
    //val replies: List<CommentType>,
    override val author: AuthorInfo,
    val isSubmitter: Boolean,
    override val relationship: Relationship,
    val subredditInfo: SubredditInfo,
    override val score: Score,
    val collapsed: Boolean,
    val createdUtc: Instant,
    val edited: Instant?,
    val mediaMetadata: Map<String, MediaMetadata>,
    val distinguished: String? = "",
) : VotableData {
    override val body: ParsedMarkdown = bodyMd
    override fun copy(relationship: Relationship?, score: Score?): CommentData =
        copy(relationship = relationship ?: this.relationship, score = score ?: this.score)

    override fun updateScore(oldLikes: Boolean?, newLikes: Boolean?): CommentData {
        return super.updateScore(oldLikes, newLikes) as CommentData
    }

    //fun updateReplies(replies: List<CommentType>): CommentData = copy(replies = replies)

    override fun toEntity(): VotableEntity {
        return VotableEntity(
            id = name,
            data = Json.encodeToString(this)
        )
    }
}
