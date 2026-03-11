package com.sofamaniac.crabir.data.remote.dto

import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiData(
    val name: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("description_md") val descriptionMd: String,
    @SerialName("description_html") val descriptionHtml: String,
    @SerialName("icon_url") val iconUrl: String,
    @SerialName("subreddits") val subreddits: List<SubredditInfo>,
    @SerialName("path") val permalink: String,
    @SerialName("num_subscribers") val subscribers: Int,
    @SerialName("copied_from") val copiedFrom: String? = null,
    @SerialName("can_edit") val canEdit: Boolean = false,
    @SerialName("created_utc") val createdUtc: Double,
    @SerialName("created") val created: Double,
    @SerialName("visibility") val visibility: String,
    @SerialName("key_color") val keyColor: String? = null,

    )

@Serializable
data class SubredditInfo(
    val name: String,
    val data: SubredditDetails
)

