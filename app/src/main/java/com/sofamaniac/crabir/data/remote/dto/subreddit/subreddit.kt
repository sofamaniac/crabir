/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:44 PM
 *
 */

package com.sofamaniac.crabir.data.remote.dto.subreddit

import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.DataInterface
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.mappie.api.ObjectMappie

@Serializable
@JvmInline
value class SubredditId(val id: String)

@Serializable
data class CommentContributionSettings(
    @SerialName("allowed_media_types") val allowedMediaTypes: List<String>? = null
)

fun dummySubredditData(): SubredditDTO {
    return SubredditDTO(
        displayName = "",
        id = "",
        iconImg = "",
        primaryColor = "black",
        keyColor = "black",
        name = Fullname(""),
    )
}
// TODO sometimes, id, display_name and icon_img are missing

@Serializable
data class SubredditDTO(
    @SerialName("user_flair_background_color")
    val userFlairBackgroundColor: String? = null,
    @SerialName("submit_text_html")
    val submitTextHtml: String? = null,
    @SerialName("user_is_banned")
    val userIsBanned: Boolean = false,
    @SerialName("free_form_reports")
    val freeFormReports: Boolean = false,
    @SerialName("wiki_enabled")
    val wikiEnabled: Boolean? = null,
    @SerialName("user_is_muted")
    val userIsMuted: Boolean = false,
    @SerialName("user_can_flair_in_sr")
    val userCanFlairInSr: Boolean? = null,
    /** The name of the subreddit (e.g. "unixporn") */
    @SerialName("display_name")
    val displayName: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("allow_galleries")
    val allowGalleries: Boolean = false,
    @SerialName("icon_size")
    val iconSize: List<Int>? = null,
    /** Primary color in the form "#ffffff" */
    @SerialName("primary_color")
    val primaryColor: String? = null,
    @SerialName("active_user_count")
    val activeUserCount: Int? = null,
    @SerialName("icon_img")
    val iconImg: String? = null,
    /** The name of the subreddit with the 'r/' prefix (e.g. "r/unixporn") */
    @SerialName("display_name_prefixed")
    val displayNamePrefixed: String = "",
    @SerialName("accounts_active")
    val accountsActive: Int? = null,
    @SerialName("public_traffic")
    val publicTraffic: Boolean = false,
    @SerialName("subscribers")
    val subscribers: Int = 0,
    @SerialName("videostream_links_count")
    val videostreamLinksCount: Int = 0,
    /** The string "t2_[id]" */
    @SerialName("name")
    override val name: Fullname,
    @SerialName("quarantine")
    val quarantine: Boolean = false,
    @SerialName("hide_ads")
    val hideAds: Boolean = false,
    @SerialName("prediction_leaderboard_entry_type")
    val predictionLeaderboardEntryType: Int = 0,
    @SerialName("emojis_enabled")
    val emojisEnabled: Boolean = false,
    @SerialName("advertiser_category")
    val advertiserCategory: String = "",
    @SerialName("public_description")
    val publicDescription: String = "",
    @SerialName("comment_score_hide_mins")
    val commentScoreHideMins: Int = 0,
    @SerialName("allow_predictions")
    val allowPredictions: Boolean = false,
    /** Escaped HTML url */
    @SerialName("community_icon")
    val communityIcon: String? = null,
    @SerialName("original_content_tag_enabled")
    val originalContentTagEnabled: Boolean = false,
    @SerialName("community_reviewed")
    val communityReviewed: Boolean = false,
    @SerialName("description_html")
    val descriptionHtml: String? = null,
    @SerialName("spoilers_enabled")
    val spoilersEnabled: Boolean = false,
    @SerialName("comment_contribution_settings")
    val commentContributionSettings: CommentContributionSettings? = null,
    @SerialName("allow_talks")
    val allowTalks: Boolean = false,
    @SerialName("user_flair_position")
    val userFlairPosition: String? = null,
    @SerialName("all_original_content")
    val allOriginalContent: Boolean = false,
    @SerialName("has_menu_widget")
    val hasMenuWidget: Boolean = false,
    @SerialName("key_color")
    val keyColor: String = "",
    @SerialName("can_assign_user_flair")
    val canAssignUserFlair: Boolean = false,
    @SerialName("created")
    val created: Double = 0.0,
    @SerialName("show_media_preview")
    val showMediaPreview: Boolean = false,
    @SerialName("submission_type")
    val submissionType: String = "",
    @SerialName("user_is_subscriber")
    val userIsSubscriber: Boolean = false,
    @SerialName("user_has_favorited")
    val userHasFavorited: Boolean = false,
    @SerialName("allowed_media_in_comments")
    val allowedMediaInComments: List<String> = emptyList(),
    @SerialName("allow_videogifs")
    val allowVideogifs: Boolean = false,
    @SerialName("should_archive_posts")
    val shouldArchivePosts: Boolean = false,
    @SerialName("user_flair_type")
    val userFlairType: String? = null,
    @SerialName("allow_polls")
    val allowPolls: Boolean = false,
    @SerialName("collapse_deleted_comments")
    val collapseDeletedComments: Boolean = false,
    @SerialName("emojis_custom_size")
    val emojisCustomSize: List<Int>? = null,
    @SerialName("public_description_html")
    val publicDescriptionHtml: String? = null,
    @SerialName("allow_videos")
    val allowVideos: Boolean = false,
    @SerialName("notification_level")
    val notificationLevel: String? = null,
    @SerialName("should_show_media_in_comments_setting")
    val shouldShowMediaInCommentsSetting: Boolean = false,
    @SerialName("can_assign_link_flair")
    val canAssignLinkFlair: Boolean = false,
    @SerialName("accounts_active_is_fuzzed")
    val accountsActiveIsFuzzed: Boolean = false,
    @SerialName("allow_prediction_contributors")
    val allowPredictionContributors: Boolean = false,
    @SerialName("link_flair_position")
    val linkFlairPosition: String = "",
    @SerialName("user_sr_flair_enabled")
    val userSrFlairEnabled: Boolean? = null,
    @SerialName("user_flair_enabled_in_sr")
    val userFlairEnabledInSr: Boolean = false,
    @SerialName("allow_discovery")
    val allowDiscovery: Boolean = false,
    @SerialName("accept_followers")
    val acceptFollowers: Boolean = false,
    @SerialName("user_sr_theme_enabled")
    val userSrThemeEnabled: Boolean = false,
    @SerialName("link_flair_enabled")
    val linkFlairEnabled: Boolean = false,
    @SerialName("disable_contributor_requests")
    val disableContributorRequests: Boolean = false,
    @SerialName("subreddit_type")
    val subredditType: String = "",

    // BANNER
    @SerialName("banner_background_image")
    val bannerBackgroundImage: String? = null,
    @SerialName("banner_img")
    val bannerImg: String = "",
    @SerialName("banner_background_color")
    val bannerBackgroundColor: String? = null,
    @SerialName("banner_size")
    val bannerSize: List<Int>? = null,
    @SerialName("mobile_banner_image")
    val mobileBannerImage: String? = null,


    @SerialName("show_media")
    val showMedia: Boolean = false,
    @SerialName("user_is_moderator")
    val userIsModerator: Boolean = false,
    @SerialName("over18")
    val over18: Boolean = false,

    // HEADER
    @SerialName("header_img")
    val headerImg: String? = null,
    @SerialName("header_title")
    val headerTitle: String? = null,
    @SerialName("header_size")
    val headerSize: List<Int>? = null,

    // SUBMIT TEXT
    @SerialName("submit_text_label")
    val submitTextLabel: String = "",
    @SerialName("submit_text")
    val submitText: String = "",
    @SerialName("submit_link_label")
    val submitLinkLabel: String = "",

    @SerialName("description")
    val description: String = "",
    @SerialName("allow_images")
    val allowImages: Boolean = false,
    @SerialName("lang")
    val lang: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("created_utc")
    val createdUtc: Double = 0.0,
    @SerialName("user_is_contributor")
    val userIsContributor: Boolean = false,
    @SerialName("allow_predictions_tournament")
    val allowPredictionsTournament: Boolean = false,
    @SerialName("id")
    override val id: String,
    @SerialName("restrict_posting")
    val restrictPosting: Boolean = false,
    @SerialName("restrict_commenting")
    val restrictCommenting: Boolean = false,
) : DataInterface {
    val icon: SubredditIcon =
        when {
            !communityIcon.isNullOrBlank() -> SubredditIcon.Icon(communityIcon)
            !iconImg.isNullOrBlank() -> SubredditIcon.Icon(iconImg)
            keyColor.isNotBlank() -> SubredditIcon.Color(keyColor)
            !primaryColor.isNullOrBlank() -> SubredditIcon.Color(primaryColor)
            else -> SubredditIcon.Color("black")
        }
}


object SubredditDetailsMapper : ObjectMappie<SubredditDTO, SubredditData>() {
    override fun map(from: SubredditDTO): SubredditData = mapping {
        SubredditData::defaultSet fromValue false
        SubredditData::previousNames fromValue emptyList()
        SubredditData::iconColor fromProperty from::keyColor
    }
}

