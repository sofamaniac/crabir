package com.sofamaniac.crabir.data.remote.dto

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetails
import com.sofamaniac.crabir.domain.model.CommunityData
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "multireddits")
data class MultiData(
    @PrimaryKey
    override val name: Fullname,
    @SerialName("display_name") override val displayName: String,
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

    ) : CommunityData {
    @Ignore
    override val displayNamePrefixed: String = "m/$displayName"

    @Ignore
    override val id: String = name.name
}

@Serializable
data class SubredditInfo(
    val name: String,
    val data: SubredditDetails
)

