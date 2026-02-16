/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.comment

import com.sofamaniac.reboost.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditId
import com.sofamaniac.reboost.data.remote.utils.EmptyStringOrListingSerializer
import com.sofamaniac.reboost.data.remote.utils.FalseOrTimestampSerializer
import com.sofamaniac.reboost.data.remote.utils.InstantAsFloatSerializer
import com.sofamaniac.reboost.domain.model.AuthorInfo
import com.sofamaniac.reboost.domain.model.CommentData
import com.sofamaniac.reboost.domain.model.CommentType
import com.sofamaniac.reboost.domain.model.Flair
import com.sofamaniac.reboost.domain.model.Relationship
import com.sofamaniac.reboost.domain.model.Score
import com.sofamaniac.reboost.domain.model.SubredditInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.mappie.api.ObjectMappie
import kotlin.time.Instant

@Serializable
@JvmInline
value class CommentId(val id: String)

@Serializable
@JvmInline
value class CommentFullname(val id: String)

@Serializable
data class CommentDTO(
    val id: String,
    val name: String,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    val depth: Int,
    @SerialName("parent_id")
    val parentId: String,
    val permalink: String,
    @Serializable(with = EmptyStringOrListingSerializer::class)
    val replies: Thing.Listing<Thing>,
    val media_metadata: Map<String, MediaMetadata> = emptyMap(),

    // ================================================ //
    // AUTHOR INFORMATION
    // ================================================ //
    val author: String = "[deleted]",
    val author_fullname: String = "[deleted]",
    val author_is_blocked: Boolean = false,
    val author_patreon_flair: Boolean = false,
    val author_premium: Boolean = false,
    // Author flair
    val author_flair_background_color: String? = null,
    val author_flair_css_class: String? = null,
    val author_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    val author_flair_template_id: String? = null,
    val author_flair_text: String? = null,
    val author_flair_text_color: String? = null,
    val author_flair_type: String? = null,

    val saved: Boolean,
    val likes: Boolean? = null,
    val score: Int,
    val downs: Int,
    val ups: Int,

    val subreddit: String,
    val subreddit_id: String,
    val subreddit_name_prefixed: String,
    val subreddit_type: String,


    @Serializable(with = InstantAsFloatSerializer::class)
    val approved_at_utc: Instant? = null,
    val approved_by: String? = null,
    val archived: Boolean,
    val all_awardings: List<String> = emptyList(),
    val associated_award: String? = null,
    val awarders: List<String> = emptyList(),
    @Serializable(with = InstantAsFloatSerializer::class)
    val banned_at_utc: Instant? = null,
    val banned_by: String? = null,
    val can_gild: Boolean,
    val can_mod_post: Boolean,
    val collapsed: Boolean,
    val collapsed_because_crowd_control: Boolean? = null,
    val collapsed_reason: String? = null,
    // TODO Replace with enum
    val collapsed_reason_code: String? = null,
    val comment_type: String? = null,
    val controversiality: Int,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created: Instant,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created_utc: Instant,
    val distinguished: String? = null,
    @Serializable(with = FalseOrTimestampSerializer::class)
    val edited: Instant? = null,
    val gilded: Int,
    // FIXME
    //val gildings: List<String>,
    val is_submitter: Boolean,
    val link_id: String,
    val locked: Boolean,
    val mod_note: String? = null,
    val mod_reason_by: String? = null,
    val mod_reason_title: String? = null,
    val mod_reports: List<String>,
    val no_follow: Boolean,
    val num_reports: Int? = null,
    val removal_reason: String? = null,
    // val report_reasons: String? = null,
    val score_hidden: Boolean,
    val send_replies: Boolean,
    val stickied: Boolean,
    val top_awarded_type: String? = null,
    val total_awards_received: Int,
    val treatment_tags: List<String>,
    val unrepliable_reason: String?,
    val user_reports: List<String>
)

object CommentDataMapper : ObjectMappie<CommentDTO, CommentData>() {
    override fun map(from: CommentDTO) = mapping {
        CommentData::name fromProperty from::name
        CommentData::id fromProperty from::id
        CommentData::parentId fromProperty from::parentId
        CommentData::depth fromProperty from::depth
        CommentData::author fromValue from.toAuthorInfo()
        CommentData::bodyMd fromProperty from::body
        CommentData::bodyHtml fromProperty from::bodyHtml
        CommentData::relationship fromValue from.toRelationship()
        CommentData::permalink fromProperty from::permalink
        CommentData::score fromValue from.toScore()
        CommentData::subredditInfo fromValue from.toSubredditInfo()
        CommentData::replies fromValue from.mapReplies()
        CommentData::createdUtc fromProperty from::created_utc
        CommentData::mediaMetadata fromProperty from::media_metadata
    }

}

private fun CommentDTO.mapReplies(): List<CommentType> = replies.data.children.map {
    when (it) {
        is Thing.Comment -> CommentType.Comment(CommentDataMapper.map(it.data))
        is Thing.More -> CommentType.More(it)
        else -> throw IllegalArgumentException("Unknown comment type: ${it.javaClass.name}")
    }
}


private fun CommentDTO.toAuthorInfo() = AuthorInfo(
    username = author,
    flair = this.toAuthorFlair(),
    authorFullname = author_fullname,
    isAuthorBlocked = author_is_blocked,
    hasPatreonFlair = author_patreon_flair,
    isAuthorPremium = author_premium,
)

private fun CommentDTO.toAuthorFlair() = Flair(
    text = author_flair_text ?: "",
    backgroundColor = author_flair_background_color ?: "",
    textColor = author_flair_text_color ?: "",
    richText = author_flair_richtext,
    type = author_flair_type ?: ""
)

private fun CommentDTO.toRelationship() = Relationship(
    clicked = false,
    visited = false,
    saved = saved,
    liked = likes
)


private fun CommentDTO.toScore() = Score(
    ups = ups,
    downs = downs,
    score = score,
    hideScore = score_hidden,
    upvoteRatio = ups.toDouble() / (ups + downs).toDouble()
)

private fun CommentDTO.toSubredditInfo() = SubredditInfo(
    name = subreddit,
    subredditId = SubredditId(subreddit_id),
    subredditPrefixed = subreddit_name_prefixed,
    subredditSubscribers = 0,
    subredditType = subreddit_type
)