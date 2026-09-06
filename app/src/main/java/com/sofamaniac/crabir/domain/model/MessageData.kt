package com.sofamaniac.crabir.domain.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.data.remote.dto.MessageDTO
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDTO
import com.sofamaniac.crabir.domain.repository.DataInterface
import kotlinx.serialization.Serializable
import kotlin.time.Instant

sealed class MessageData : DataInterface {
    data class Message(val message: MessageDTO) : MessageData() {
        override val id: String = message.id
        override val name: Fullname = message.name
    }

    data class Comment(val comment: CommentDTO) : MessageData() {
        override val id: String = comment.id
        override val name: Fullname = comment.name
    }
}

@Serializable
data class ParentInfo(
    val name: Fullname,
    val title: String? = null,
    val subreddit: String? = null,
    val subredditPrefixed: String? = null,
)


@Entity(tableName = "inboxTable")
@Serializable
data class Message(
    @PrimaryKey
    override val name: Fullname,
    override val id: String,
    val author: String?,
    val authorFullname: Fullname?,
    val body: String,
    val bodyHtml: String,
    val context: String,
    val createdUtc: Instant,
    val dest: String,
    val distinguished: String? = null,
    val firstMessage: Long? = null,
    val firstMessageName: Fullname? = null,
    val likes: Boolean?,
    val new: Boolean,
    val numComments: Int?,
    val replies: String,
    val score: Int,
    val subject: String,
    val parent: ParentInfo? = null,
    val type: MessageType,
    val wasComment: Boolean,
) : DataInterface

@Serializable
sealed class MessageType {
    @Serializable
    object CommentReply : MessageType()

    @Serializable
    object PostReply : MessageType()

    @Serializable
    object Message : MessageType()

    @Serializable
    object Mention : MessageType()

    @Serializable
    class Unknown(val label: String) : MessageType()

    companion object {
        fun fromString(type: String): MessageType {
            return when (type) {
                "comment_reply" -> CommentReply
                "message" -> Message
                "username_mention" -> Mention
                "post_reply" -> PostReply
                else -> {
                    Log.e("MessageType", "Unknown message type: $type")
                    Unknown(type)
                }
            }
        }
    }
}
