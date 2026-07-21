/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.dto.comment

import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.emptyListing
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditId
import com.sofamaniac.crabir.data.remote.utils.EmptyStringOrListingSerializer
import com.sofamaniac.crabir.data.remote.utils.FalseOrTimestampSerializer
import com.sofamaniac.crabir.data.remote.utils.InstantAsFloatSerializer
import com.sofamaniac.crabir.domain.model.AuthorInfo
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Flair
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import com.sofamaniac.crabir.domain.model.Relationship
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.model.Score
import com.sofamaniac.crabir.domain.model.SubredditInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.mappie.api.ObjectMappie
import java.util.Collections
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
    val name: Fullname,
    val body: String,
    @SerialName("body_html")
    val bodyHtml: String,
    @SerialName("rtjson") val richtext: RichtextDocument = RichtextDocument(Collections.emptyList()),
    val depth: Int = -1,
    @SerialName("parent_id")
    val parentId: Fullname,
    val permalink: String = "",
    @Serializable(with = EmptyStringOrListingSerializer::class)
    val replies: Thing.Listing<Thing> = emptyListing(),
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

    val saved: Boolean = false,
    val likes: Boolean? = null,
    val score: Int = 0,
    val downs: Int = 0,
    val ups: Int = 0,

    val subreddit: String = "",
    val subreddit_id: String = "",
    val subreddit_name_prefixed: String = "",
    val subreddit_type: String = "",

    // FIELD WHEN MESSAGE
    val subject: String? = null,
    val type: String? = null,
    val context: String? = null,


    @Serializable(with = InstantAsFloatSerializer::class)
    val approved_at_utc: Instant? = null,
    val approved_by: String? = null,
    val archived: Boolean = false,
    val all_awardings: List<String> = emptyList(),
    val associated_award: String? = null,
    val awarders: List<String> = emptyList(),
    @Serializable(with = InstantAsFloatSerializer::class)
    val banned_at_utc: Instant? = null,
    val banned_by: String? = null,
    val can_gild: Boolean = false,
    val can_mod_post: Boolean = false,
    val collapsed: Boolean = false,
    val collapsed_because_crowd_control: Boolean? = null,
    val collapsed_reason: String? = null,
    // TODO Replace with enum
    val collapsed_reason_code: String? = null,
    val comment_type: String? = null,
    val controversiality: Int = 0,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created: Instant,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created_utc: Instant,
    val distinguished: String? = null,
    @Serializable(with = FalseOrTimestampSerializer::class)
    val edited: Instant? = null,
    val gilded: Int = 0,
    // FIXME
    //val gildings: List<String>,
    val is_submitter: Boolean = false,
    val link_id: String = "",
    val locked: Boolean = false,
    val mod_note: String? = null,
    val mod_reason_by: String? = null,
    val mod_reason_title: String? = null,
    val mod_reports: List<String> = emptyList(),
    val no_follow: Boolean = false,
    val num_reports: Int? = null,
    val removal_reason: String? = null,
    // val report_reasons: String? = null,
    val score_hidden: Boolean = false,
    val send_replies: Boolean = false,
    val stickied: Boolean = false,
    val top_awarded_type: String? = null,
    val total_awards_received: Int = 0,
    val treatment_tags: List<String> = emptyList(),
    val unrepliable_reason: String? = null,
    val user_reports: List<String> = emptyList(),
)

object CommentDataMapper : ObjectMappie<CommentDTO, CommentData>() {
    override fun map(from: CommentDTO) = mapping {
        CommentData::name fromProperty from::name
        CommentData::id fromProperty from::id
        CommentData::parentId fromProperty from::parentId
        CommentData::depth fromProperty from::depth
        CommentData::author fromValue from.toAuthorInfo()
        CommentData::bodyMd fromValue from.markdown()
        CommentData::bodyHtml fromProperty from::bodyHtml
        CommentData::relationship fromValue from.toRelationship()
        CommentData::permalink fromProperty from::permalink
        CommentData::score fromValue from.toScore()
        CommentData::subredditInfo fromValue from.toSubredditInfo()
        //CommentData::replies fromValue from.mapReplies()
        CommentData::replies fromValue from.countReplies()
        CommentData::createdUtc fromProperty from::created_utc
        CommentData::mediaMetadata fromProperty from::media_metadata
        CommentData::isSubmitter fromProperty from::is_submitter
    }

}

private fun CommentDTO.countReplies(): Int = replies.size

private fun CommentDTO.mapReplies(): List<CommentType> = replies.data.children.map {
    when (it) {
        is Thing.Comment -> CommentType.Comment(CommentDataMapper.map(it.data))
        is Thing.More -> CommentType.More(it.data)
        else -> throw IllegalArgumentException("Unknown comment type: ${it.javaClass.name}")
    }
}

private fun CommentDTO.markdown(): ParsedMarkdown {
    return ParsedMarkdown(body, media_metadata)
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
    liked = likes,
    hidden = false,
)


private fun CommentDTO.toScore() = Score(
    ups = ups,
    downs = downs,
    score = score,
    hideScore = score_hidden,
    upvoteRatio = ups.toDouble() / (ups + downs).toDouble().coerceAtLeast(1.0)
)

private fun CommentDTO.toSubredditInfo() = SubredditInfo(
    name = subreddit,
    subredditId = SubredditId(subreddit_id),
    subredditPrefixed = subreddit_name_prefixed,
    subredditSubscribers = 0,
    subredditType = subreddit_type
)