package com.sofamaniac.crabir.data.remote.dto

import com.sofamaniac.crabir.data.remote.utils.InstantAsFloatSerializer
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class MessageDTO(
    //@SerialName("associated_awarding_id") val associatedAwardingId: String,
    val author: String?,
    @SerialName("author_fullname") val authorFullname: Fullname?,
    val body: String,
    @SerialName("body_html") val bodyHtml: String,
    val context: String,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created: Instant,
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("created_utc") val createdUtc: Instant,
    val dest: String,
    val distinguished: String? = null,
    @SerialName("first_message") val firstMessage: Long? = null,
    @SerialName("first_message_name") val firstMessageName: Fullname? = null,
    val id: String,
    val likes: Boolean?,
    val name: Fullname,
    val new: Boolean,
    @SerialName("num_comments") val numComments: Int?,
    @SerialName("parent_id") val parentId: Fullname?,
    val replies: String,
    val score: Int,
    val subject: String,
    val subreddit: SubredditInfo?,
    @SerialName("subreddit_name_prefixed") val subredditNamePrefixed: String?,
    val type: String,
    @SerialName("was_comment") val wasComment: Boolean
)