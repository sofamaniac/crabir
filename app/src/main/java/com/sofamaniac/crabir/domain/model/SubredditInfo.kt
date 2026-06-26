/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditIcon
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditId
import kotlinx.serialization.Serializable

@Serializable
data class SubredditInfo(
    val name: String,
    val subredditId: SubredditId,
    val subredditPrefixed: String,
    val subredditSubscribers: Int,
    val subredditType: String
) {
    companion object {
        val DUMMY = SubredditInfo(
            name = "DUMMY",
            subredditId = SubredditId("t3_dummy"),
            subredditPrefixed = "r/DUMMY",
            subredditSubscribers = 1_000_000,
            subredditType = "type"
        )
    }
}


@Serializable
@Entity(tableName = "subreddits")
data class SubredditData(
    @PrimaryKey
    override val id: String,
    override val name: Fullname,
    val defaultSet: Boolean,
    val bannerImg: String,
    val allowedMediaInComments: List<String>,
    val userIsBanned: Boolean?,
    val freeFormReports: Boolean?,
    val communityIcon: String?,
    val showMedia: Boolean,
    val description: ParsedMarkdown,
    val userIsMuted: Boolean?,
    override val displayName: String,
    val headerImg: String?,
    val title: String,
    val previousNames: List<String>,
    val userIsModerator: Boolean?,
    val over18: Boolean,
    val iconSize: List<Int>?,
    val primaryColor: String?,
    val iconImg: String?,
    val iconColor: String,
    val submitLinkLabel: String,
    val headerSize: List<Int>?,
    val restrictPosting: Boolean,
    val restrictCommenting: Boolean,
    val subscribers: Int,
    val submitTextLabel: String,
    val linkFlairPosition: String,
    override val displayNamePrefixed: String,
    val keyColor: String?,
    val url: String,
    val quarantine: Boolean,
    val createdUtc: Double = 0.0,
    val created: Double = 0.0,
    val bannerSize: List<Int>?,
    val userIsContributor: Boolean?,
    val acceptFollowers: Boolean,
    val publicDescription: ParsedMarkdown,
    val linkFlairEnabled: Boolean,
    val disableContributorRequests: Boolean,
    val subredditType: String,
    val userIsSubscriber: Boolean = false,
    val userHasFavorited: Boolean = false,
) : CommunityData {
    @Ignore
    val icon: SubredditIcon =
        when {
            !communityIcon.isNullOrBlank() -> SubredditIcon.Icon(communityIcon)
            !iconImg.isNullOrBlank() -> SubredditIcon.Icon(iconImg)
            !keyColor.isNullOrBlank() -> SubredditIcon.Color(keyColor)
            !primaryColor.isNullOrBlank() -> SubredditIcon.Color(primaryColor)
            else -> SubredditIcon.Color("black")
        }

}