package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.data.remote.dto.post.Preview
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetails
import com.sofamaniac.crabir.reddit.Thumbnail
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class PostData(
    override val id: String,
    override val name: Fullname,
    override val relationship: Relationship,
    val createdUtc: Instant,
    val edited: Instant?,
    val author: AuthorInfo,
    val subreddit: SubredditInfo,
    override val score: Score,
    val url: String,
    val domain: String,
    val permalink: String,
    val title: String,
    val suggestedSort: String,
    val numComments: Int,
    val over18: Boolean,
    val spoiler: Boolean,
    val sendReplies: Boolean,
    val preview: Preview?,
    val crosspostParentList: List<PostData>,
    val subredditDetails: SubredditDetails?,
    val thumbnail: Thumbnail,
    val selftext: Selftext,
    val mediaMetadata: Map<String, MediaMetadata>,
    val kind: Kind,
    val isDistinguished: Boolean,
    val linkFlair: Flair,
    val media: MediaInfo,
    val gallery: Gallery?,
    val locked: Boolean,
    val isCrosspostable: Boolean,
    val canModPost: Boolean,
    //val status: Status
) : VotableData {
    val isCrosspost: Boolean =
        crosspostParentList.isNotEmpty()

    val shortlink = "https://redd.it/$id"

    override fun copy(relationship: Relationship?, score: Score?): PostData {
        return copy(relationship = relationship ?: this.relationship, score = score ?: this.score)
    }
}