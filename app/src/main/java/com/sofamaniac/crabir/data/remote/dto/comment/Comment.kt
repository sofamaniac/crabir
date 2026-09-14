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
import com.sofamaniac.crabir.domain.model.Message
import com.sofamaniac.crabir.domain.model.MessageType
import com.sofamaniac.crabir.domain.model.ParentInfo
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
    @SerialName("media_metadata")
    val mediaMetadata: Map<String, MediaMetadata> = emptyMap(),

    // ================================================ //
    // AUTHOR INFORMATION
    // ================================================ //
    val author: String = "[deleted]",
    @SerialName("author_fullname")
    val authorFullname: Fullname = Fullname("[deleted]"),
    @SerialName("author_is_blocked")
    val authorIsBlocked: Boolean = false,
    @SerialName("author_patreon_flair")
    val authorPatreonFlair: Boolean = false,
    @SerialName("author_premium")
    val authorPremium: Boolean = false,
    // Author flair
    @SerialName("author_flair_background_color")
    val authorFlairBackgroundColor: String? = null,
    @SerialName("author_flair_css_class")
    val authorFlairCssClass: String? = null,
    @SerialName("author_flair_richtext")
    val authorFlairRichtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("author_flair_template_id")
    val authorFlairTemplateId: String? = null,
    @SerialName("author_flair_text")
    val authorFlairText: String? = null,
    @SerialName("author_flair_text_color")
    val authorFlairTextColor: String? = null,
    @SerialName("author_flair_type")
    val authorFlairType: String? = null,

    val saved: Boolean = false,
    val likes: Boolean? = null,
    val score: Int = 0,
    val downs: Int = 0,
    val ups: Int = 0,

    val subreddit: String = "",
    @SerialName("subreddit_id")
    val subredditId: String = "",
    @SerialName("subreddit_name_prefixed")
    val subredditNamePrefixed: String = "",
    @SerialName("subreddit_type")
    val subredditType: String = "",

    // FIELD WHEN MESSAGE
    val subject: String = "",
    @SerialName("link_title")
    val linkTitle: String? = null,
    val type: String? = null,
    val context: String = "",
    val new: Boolean = false,
    val dest: String = "",
    @SerialName("num_comments")
    val numComments: Int = 0,
    @SerialName("was_comment")
    val wasComment: Boolean = false,


    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("approved_at_utc")
    val approvedAtUtc: Instant? = null,
    @SerialName("approved_by")
    val approvedBy: String? = null,
    val archived: Boolean = false,
    @SerialName("all_awardings")
    val allAwardings: List<String> = emptyList(),
    @SerialName("associated_award")
    val associatedAward: String? = null,
    val awarders: List<String> = emptyList(),
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("banned_at_utc")
    val bannedAtUtc: Instant? = null,
    @SerialName("banned_by")
    val bannedBy: String? = null,
    @SerialName("can_gild")
    val canGild: Boolean = false,
    @SerialName("can_mod_post")
    val canModPost: Boolean = false,
    val collapsed: Boolean = false,
    @SerialName("collapsed_because_crowd_control")
    val collapsedBecauseCrowdControl: Boolean? = null,
    @SerialName("collapsed_reason")
    val collapsedReason: String? = null,
    // TODO Replace with enum
    @SerialName("collapsed_reason_code")
    val collapsedReasonCode: String? = null,
    @SerialName("comment_type")
    val commentType: String? = null,
    val controversiality: Int = 0,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created: Instant,
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("created_utc")
    val createdUtc: Instant,
    val distinguished: String? = null,
    @Serializable(with = FalseOrTimestampSerializer::class)
    val edited: Instant? = null,
    val gilded: Int = 0,
    // FIXME
    //val gildings: List<String>,
    @SerialName("is_submitter")
    val isSubmitter: Boolean = false,
    @SerialName("link_id")
    val linkId: String = "",
    val locked: Boolean = false,
    @SerialName("mod_note")
    val modNote: String? = null,
    @SerialName("mod_reason_by")
    val modReasonBy: String? = null,
    @SerialName("mod_reason_title")
    val modReasonTitle: String? = null,
    @SerialName("mod_reports")
    val modReports: List<String> = emptyList(),
    @SerialName("no_follow")
    val noFollow: Boolean = false,
    @SerialName("num_reports")
    val numReports: Int? = null,
    @SerialName("removal_reason")
    val removalReason: String? = null,
    // val report_reasons: String? = null,
    @SerialName("score_hidden")
    val scoreHidden: Boolean = false,
    @SerialName("send_replies")
    val sendReplies: Boolean = false,
    val stickied: Boolean = false,
    @SerialName("top_awarded_type")
    val topAwardedType: String? = null,
    @SerialName("total_awards_received")
    val totalAwardsReceived: Int = 0,
    @SerialName("treatment_tags")
    val treatmentTags: List<String> = emptyList(),
    @SerialName("unrepliable_reason")
    val unrepliableReason: String? = null,
    @SerialName("user_reports")
    val userReports: List<String> = emptyList(),
)

object CommentDataMapper : ObjectMappie<CommentDTO, CommentData>() {
    override fun map(from: CommentDTO) = mapping {
        CommentData::name fromProperty from::name
        CommentData::id fromProperty from::id
        CommentData::parentInfo fromValue from.getParentInfo()
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
        CommentData::createdUtc fromProperty from::createdUtc
        CommentData::mediaMetadata fromProperty from::mediaMetadata
        CommentData::isSubmitter fromProperty from::isSubmitter
    }

}

object CommentMessageMapper : ObjectMappie<CommentDTO, Message>() {
    override fun map(from: CommentDTO): Message = mapping {
        Message::type fromValue from.getType()
        Message::authorFullname fromProperty from::authorFullname
        Message::createdUtc fromProperty from::createdUtc
        Message::replies fromValue ""
    }
}

fun CommentDTO.getType(): MessageType {
    return MessageType.fromString(type ?: "")
}

fun CommentDTO.getParentInfo(): ParentInfo {
    val title = linkTitle ?: subject
    return ParentInfo(
        name = parentId,
        title = title,
        subreddit = subreddit,
        subredditPrefixed = subredditNamePrefixed
    )
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
    return ParsedMarkdown(body, mediaMetadata)
}


private fun CommentDTO.toAuthorInfo() = AuthorInfo(
    username = author,
    flair = this.toAuthorFlair(),
    authorFullname = authorFullname,
    isAuthorBlocked = authorIsBlocked,
    hasPatreonFlair = authorPatreonFlair,
    isAuthorPremium = authorPremium,
)

private fun CommentDTO.toAuthorFlair() = Flair(
    text = authorFlairText ?: "",
    backgroundColor = authorFlairBackgroundColor ?: "",
    textColor = authorFlairTextColor ?: "",
    richtext = authorFlairRichtext,
    type = authorFlairType ?: ""
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
    hideScore = scoreHidden,
    upvoteRatio = ups.toDouble() / (ups + downs).toDouble().coerceAtLeast(1.0)
)

private fun CommentDTO.toSubredditInfo() = SubredditInfo(
    name = subreddit,
    subredditId = SubredditId(subredditId),
    subredditPrefixed = subredditNamePrefixed,
    subredditSubscribers = 0,
    subredditType = subredditType
)
