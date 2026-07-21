/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:42 PM
 *
 */

package com.sofamaniac.crabir.data.remote.dto.post

import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetails
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetailsMapper
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditId
import com.sofamaniac.crabir.data.remote.utils.CommentSortSerializer
import com.sofamaniac.crabir.data.remote.utils.FalseOrTimestampSerializer
import com.sofamaniac.crabir.data.remote.utils.InstantAsFloatSerializer
import com.sofamaniac.crabir.data.remote.utils.MediaMetadataSerializer
import com.sofamaniac.crabir.data.remote.utils.RedditVideoSerializer
import com.sofamaniac.crabir.domain.model.AuthorInfo
import com.sofamaniac.crabir.domain.model.Flair
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Gallery
import com.sofamaniac.crabir.domain.model.MediaInfo
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.Relationship
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.model.Score
import com.sofamaniac.crabir.domain.model.Selftext
import com.sofamaniac.crabir.domain.model.SubredditInfo
import com.sofamaniac.crabir.domain.model.getKind
import com.sofamaniac.crabir.reddit.Thumbnail
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import tech.mappie.api.ObjectMappie
import java.util.Collections.emptyList
import kotlin.time.Instant


/** ID 36 of a post */
@Serializable
@JvmInline
value class PostId(val id: String)

/**
 * The string "t3_ID36" of a post
 * @see PostId
 */
@Serializable
@JvmInline
value class PostFullname(val id: String)

@Serializable
data class PostDTO(

    @SerialName("id") val id: String,
    @SerialName("name") val fullname: Fullname,
    @SerialName("url") val url: String = "",
    @SerialName("title") val title: String = "",
    @Serializable(with = CommentSortSerializer::class)
    @SerialName("suggested_sort") val suggestedSort: Sort? = null,
    @SerialName("num_comments") val numComments: Int = 0,
    @SerialName("over_18") val over18: Boolean = false,
    @SerialName("permalink") val permalink: String,
    @SerialName("post_hint") val postHint: String? = null,
    @SerialName("pinned") val pinned: Boolean = false,
    @SerialName("preview") val preview: Preview? = null,
    @SerialName("crosspost_parent_list") val crosspostParentList: List<PostDTO> = emptyList(),


    // ================================================ //
    // AUTHOR INFORMATION
    // ================================================ //
    @SerialName("author") val author: String? = "",
    @SerialName("author_fullname") val authorFullname: String = "",
    @SerialName("author_is_blocked") val authorIsBlocked: Boolean = false,
    @SerialName("author_patreon_flair") val authorPatreonFlair: Boolean = false,
    @SerialName("author_premium") val authorPremium: Boolean = false,

    // Author flair
    @SerialName("author_flair_background_color") val authorFlairBackgroundColor: String? = null,
    @SerialName("author_flair_css_class") val authorFlairCssClass: String? = null,
    @SerialName("author_flair_richtext") val authorFlairRichtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("author_flair_template_id") val authorFlairTemplateId: String? = null,
    @SerialName("author_flair_text") val authorFlairText: String? = null,
    @SerialName("author_flair_text_color") val authorFlairTextColor: String? = null,
    @SerialName("author_flair_type") val authorFlairType: String? = null,

    // Approved info
    @SerialName("approved_at_utc") val approvedAtUtc: String? = null,
    @SerialName("approved_by") val approvedBy: String? = null,

    // Ban Info
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("banned_at_utc") val bannedAtUtc: Instant? = null,
    @SerialName("banned_by") val bannedBy: String? = null,

    // ================================================ //
    // SUBREDDIT INFORMATION
    // ================================================ //
    @SerialName("subreddit") val subreddit: String,
    @SerialName("subreddit_id") val subredditId: SubredditId,
    @SerialName("subreddit_name_prefixed") val subredditNamePrefixed: String = "",
    @SerialName("subreddit_subscribers") val subredditSubscribers: Int = 0,
    @SerialName("subreddit_type") val subredditType: String = "",
    @SerialName("sr_detail") val subredditDetails: SubredditDetails? = null,

    // ================================================ //
    // THUMBNAIL INFORMATION
    // ================================================ //
    /** Can either be an URL, "self", "spoiler" */
    @SerialName("thumbnail") val thumbnailUrl: String? = null,
    @SerialName("thumbnail_height") val thumbnailHeight: Int? = null,
    @SerialName("thumbnail_width") val thumbnailWidth: Int? = null,

    // ================================================ //
    // SCORE INFORMATION
    // ================================================ //
    @SerialName("downs") val downs: Int = 0,
    @SerialName("score") val scoreInt: Int = 0,
    @SerialName("ups") val ups: Int = 0,
    @SerialName("upvote_ratio") val upvoteRatio: Double = 0.0,
    @SerialName("hide_score") val hideScore: Boolean = false,

    // Awards
    @SerialName("all_awardings") val allAwardings: List<String> = emptyList(),
    @SerialName("awarders") val awarders: List<String> = emptyList(),
    @SerialName("gilded") val gilded: Int = 0,
    @SerialName("gildings") val gildings: Map<String, Int> = emptyMap(),
    @SerialName("top_awarded_type") val topAwardedType: String? = null,
    @SerialName("total_awards_received") val totalAwardsReceived: Int = 0,

    // Selftext
    @SerialName("selftext") val selftextRaw: String = "",
    @SerialName("selftext_html") val selftextHtml: String? = null,
    @SerialName("rtjson") val richtext: RichtextDocument = RichtextDocument(emptyList()),

    // Creation Info
    @SerialName("created") val created: Double = 0.0,
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("created_utc") val createdUtc: Instant = Instant.DISTANT_FUTURE,

    // Relationship
    @SerialName("clicked") val clicked: Boolean = false,
    @SerialName("visited") val visited: Boolean = false,
    @SerialName("likes") var likes: Boolean? = null,
    @SerialName("saved") var saved: Boolean = false,

    // Status
    @SerialName("archived") val archived: Boolean = false,
    @Serializable(with = FalseOrTimestampSerializer::class)
    @SerialName("edited") val edited: Instant? = null,
    @SerialName("hidden") val hidden: Boolean = false,
    @SerialName("locked") val locked: Boolean = false,
    @SerialName("is_crosspostable") val isCrosspostable: Boolean = false,
    @SerialName("is_created_from_ads_ui") val isCreatedFromAdsUi: Boolean = false,
    @SerialName("can_gild") val gildable: Boolean = false,
    @SerialName("can_mod_post") val canModPost: Boolean = false,
    @SerialName("spoiler") val spoiler: Boolean = false,
    @SerialName("stickied") val stickied: Boolean = false,

    @SerialName("category") val category: String? = null,
    @SerialName("content_categories") val contentCategories: List<String>? = null,
    @SerialName("contest_mode") val contestMode: Boolean = false,
    @SerialName("discussion_type") val discussionType: String? = null,
    @SerialName("distinguished") val distinguished: String? = null,
    @SerialName("domain") val domain: String = "",

    // Kind
    @SerialName("is_gallery") val isGallery: Boolean = false,
    @SerialName("is_meta") val isMeta: Boolean = false,
    @SerialName("is_original_content") val isOriginalContent: Boolean = false,
    @SerialName("is_reddit_media_domain") val isRedditMediaDomain: Boolean = false,
    @SerialName("is_self") val isSelfPost: Boolean = false,
    @SerialName("is_video") val isVideo: Boolean = false,

    // ================================================ //
    // FLAIR INFORMATION
    // ================================================ //
    @SerialName("link_flair_background_color") val linkFlairBackgroundColor: String? = null,
    @SerialName("link_flair_css_class") val linkFlairCssClass: String? = null,
    @SerialName("link_flair_richtext") val linkFlairRichtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("link_flair_text") val linkFlairText: String? = null,
    @SerialName("link_flair_text_color") val linkFlairTextColor: String? = null,
    @SerialName("link_flair_type") val linkFlairType: String? = null,

    // Media
    @SerialName("media") val media: Media? = null,
    @SerialName("media_embed") val mediaEmbed: Map<String, String> = emptyMap(),
    @SerialName("gallery_data") val galleryData: GalleryData? = null,
    @SerialName("media_metadata") val mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    @SerialName("media_only") val isMediaOnly: Boolean = false,
    @SerialName("secure_media") val secureMedia: Media? = null,
    @SerialName("secure_media_embed") val secureMediaEmbed: Map<String, String> = emptyMap(),

    // ================================================ //
    // MODERATION INFORMATION
    // ================================================ //
    @SerialName("mod_note") val modNote: String? = null,
    @SerialName("mod_reason_by") val modReasonBy: String? = null,
    @SerialName("mod_reason_title") val modReasonTitle: String? = null,
    @SerialName("mod_reports") val modReports: List<String> = emptyList(),
    // Removal info
    @SerialName("removal_reason") val removalReason: String? = null,
    @SerialName("removed_by") val removedBy: String? = null,
    @SerialName("removed_by_category") val removedByCategory: String? = null,

    // ================================================ //
    // OTHER INFORMATION
    // ================================================ //
    @SerialName("no_follow") val noFollow: Boolean = false,
    @SerialName("num_crossposts") val numCrossposts: Int = 0,
    @SerialName("num_reports") val numReports: Int? = null,
    @SerialName("pwls") val pwls: Int? = null,
    @SerialName("quarantine") val quarantine: Boolean = false,
    @SerialName("report_reasons") val reportReasons: List<String>? = null,
    @SerialName("send_replies") val sendReplies: Boolean = false,
    @SerialName("treatment_tags") val treatmentTags: List<String> = emptyList(),
    @SerialName("user_reports") val userReports: List<String> = emptyList(),
    @SerialName("view_count") val viewCount: String? = null,
    @SerialName("wls") val wls: Int? = null,
    @SerialName("allow_live_comments") val allowLiveComments: Boolean = false,
)

private fun PostDTO.toAuthorInfo() = AuthorInfo(
    username = author ?: "",
    flair = this.toAuthorFlair(),
    authorFullname = authorFullname,
    isAuthorBlocked = authorIsBlocked,
    hasPatreonFlair = authorPatreonFlair,
    isAuthorPremium = authorPremium
)

private fun PostDTO.toAuthorFlair() = Flair(
    text = authorFlairText ?: "",
    backgroundColor = authorFlairBackgroundColor ?: "",
    textColor = authorFlairTextColor ?: "",
    richText = authorFlairRichtext,
    type = authorFlairType ?: ""
)

private fun PostDTO.toSubredditInfo() = SubredditInfo(
    name = subreddit,
    subredditId = subredditId,
    subredditPrefixed = subredditNamePrefixed,
    subredditSubscribers = subredditSubscribers,
    subredditType = subredditType
)

private fun PostDTO.toThumbnail() = Thumbnail(
    uri = thumbnailUrl ?: "",
    width = thumbnailWidth ?: 0,
    height = thumbnailHeight ?: 0
)

private fun PostDTO.toScore() = Score(
    ups = ups,
    downs = downs,
    score = scoreInt,
    upvoteRatio = upvoteRatio,
    hideScore = hideScore
)

private fun PostDTO.toSelftext() = Selftext(
    markdown = ParsedMarkdown(selftextRaw, mediaMetadata),
    html = selftextHtml ?: "",
    richtext = richtext
)

private fun PostDTO.toLinkFlair() = Flair(
    text = linkFlairText ?: "",
    backgroundColor = linkFlairBackgroundColor ?: "",
    textColor = linkFlairTextColor ?: "",
    richText = linkFlairRichtext,
    type = linkFlairType ?: ""
)

private fun PostDTO.toMediaInfo() = MediaInfo(
    media = secureMedia ?: media,
    mediaEmbed = secureMediaEmbed.ifEmpty { mediaEmbed },
    mediaOnly = isMediaOnly
)

private fun PostDTO.toGallery() = Gallery(
    images = galleryData?.items ?: emptyList(),
    mediaMetadata = mediaMetadata
)


private fun PostDTO.toRelationship() = Relationship(
    clicked = clicked,
    visited = visited,
    saved = saved,
    liked = likes,
    hidden = hidden
)

private fun PostDTO.getPreview() = preview ?: crosspostParentList.firstOrNull()?.preview

private fun PostDTO.getGalleryData(): Gallery? {
    val gallery = toGallery()
    if (gallery.images.isNotEmpty()) return gallery
    return crosspostParentList.firstOrNull()?.getGalleryData()
}

private fun PostDTO.isDistinguished() = pinned || stickied || distinguished == "moderator"

object PostDataMapper : ObjectMappie<PostDTO, PostData>() {

    override fun map(from: PostDTO) = mapping {

        PostData::name fromProperty from::fullname

        PostData::url fromProperty from::url
        PostData::suggestedSort fromProperty from::suggestedSort
        PostData::preview fromValue from.getPreview()
        PostData::crosspostParentList fromProperty from::crosspostParentList

        PostData::author fromValue from.toAuthorInfo()
        PostData::subreddit fromValue from.toSubredditInfo()
        PostData::subredditDetails fromProperty from::subredditDetails via SubredditDetailsMapper
        PostData::thumbnail fromValue from.toThumbnail()
        PostData::score fromValue from.toScore()
        PostData::selftext fromValue from.toSelftext()
        PostData::linkFlair fromValue from.toLinkFlair()
        PostData::media fromValue from.toMediaInfo()
        PostData::relationship fromValue from.toRelationship()
        PostData::kind fromValue getKind(from)

        PostData::createdUtc fromProperty from::createdUtc
        PostData::edited fromProperty from::edited

        PostData::gallery fromValue from.getGalleryData()
        PostData::isDistinguished fromValue from.isDistinguished()
    }
}


//FIXME
@Serializable
data class Media(
    @Serializable(with = RedditVideoSerializer::class)
    @SerialName("reddit_video")
    val redditVideo: RedditVideo? = null,
    val oembed: OEmbed? = null,
    val type: String? = null,
)

@Serializable
data class OEmbed(
    @SerialName("provider_url") val providerUrl: String? = null,
    @SerialName("version") val version: String = "",
    @SerialName("title") val title: String = "",
    /** One of "video" */
    @SerialName("type") val type: String = "",
    @SerialName("height") val height: Int = 0,
    @SerialName("width") val width: Int = 0,
    @SerialName("html") val html: String = "",
    @SerialName("provider_name") val providerName: String = "",

    @SerialName("thumbnail_width") val thumbnailWidth: Int = 0,
    @SerialName("thumbnail_height") val thumbnailHeight: Int = 0,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
)


@Serializable
data class RedditVideo(
    /** Use this url to get the video */
    @SerialName("fallback_url") val fallbackUrl: String,
    val width: Int = 0,
    val height: Int = 0,
    @SerialName("scrubber_media_url") val scrubberMediaUrl: String,
    @SerialName("dash_url") val dashUrl: String,
    /** Duration in seconds */
    val duration: Int,
    @SerialName("hls_url") val hlsUrl: String? = null,
    @SerialName("is_gif") val isGif: Boolean = false,
) {
    fun toMediaResource() =
        MediaResource(dashUrl, width.toFloat() / height.toFloat(), width, height)
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = MediaMetadataSerializer::class)
@JsonClassDiscriminator("e")
sealed class MediaMetadata() {

    abstract val ratio: Float
    abstract val width: Int
    abstract val height: Int

    abstract fun toMediaResource(): MediaResource?


    @Serializable
    @SerialName("Image")
    data class Image(
        @SerialName("id") val id: String? = null,
        /** Something like "image/jpeg" */
        @SerialName("m") val mime: String? = null,
        @SerialName("p") val preview: List<MediaPreview> = emptyList(),
        @SerialName("s") val source: MediaPreview? = null,
        @SerialName("o") val obfuscated: List<MediaPreview> = emptyList(),
    ) : MediaMetadata() {
        override val ratio: Float
            get() = source?.ratio ?: 1f
        override val width: Int
            get() = source?.width ?: 0
        override val height: Int
            get() = source?.height ?: 0

        override fun toMediaResource() = MediaResource(source!!.url!!, ratio, width, height)
    }

    @Serializable
    @SerialName("AnimatedImage")
    data class Gif(
        @SerialName("id") val id: String? = null,
        /** Something like "image/jpeg" */
        @SerialName("m") val mime: String? = null,
        @SerialName("p") val preview: List<MediaPreview> = emptyList(),
        @SerialName("s") val source: MediaPreviewGifSource? = null,
        @SerialName("o") val obfuscated: List<MediaPreview> = emptyList(),
    ) : MediaMetadata() {
        override val ratio: Float
            get() = source?.ratio ?: 1f
        override val width: Int
            get() = source?.width ?: 0
        override val height: Int
            get() = source?.height ?: 0

        override fun toMediaResource() = MediaResource(source!!.mp4Url!!, ratio, width, height)
    }

    @Serializable
    @SerialName("Invalid")
    object Invalid : MediaMetadata() {
        override val ratio: Float
            get() = 0f
        override val width: Int
            get() = 0
        override val height: Int
            get() = 0

        override fun toMediaResource(): MediaResource? = null
    }
}

@Serializable
data class MediaPreviewGifSource(
    @SerialName("gif") val gifUrl: String? = null,
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0,
    @SerialName("mp4") val mp4Url: String? = null,
) {
    val ratio: Float
        get() = width.toFloat() / height.toFloat()
}


@Serializable
data class MediaPreview(
    /** Url of the preview */
    @SerialName("u") val url: String? = null,
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0
) {
    val ratio: Float
        get() = width.toFloat() / height.toFloat()

    fun toMediaResource(): MediaResource {
        return MediaResource(url!!, ratio, width, height)
    }
}
