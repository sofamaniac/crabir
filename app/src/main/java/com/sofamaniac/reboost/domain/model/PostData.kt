package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.post.Preview
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditDetails
import com.sofamaniac.reboost.reddit.Thumbnail
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
class PostData(
    override val id: String,
    override val name: String,
    override var relationship: Relationship,
    val createdUtc: Instant,
    val edited: Instant?,
    val author: AuthorInfo,
    val subreddit: SubredditInfo,
    override var score: Score,
    val url: String,
    val domain: String,
    val permalink: String,
    val title: String,
    val suggestedSort: String,
    val numComments: Int,
    val over18: Boolean,
    val preview: Preview?,
    val crosspostParentList: List<PostData>,
    val subredditDetails: SubredditDetails?,
    val thumbnail: Thumbnail,
    val selftext: Selftext,
    val kind: Kind,
    val linkFlair: Flair,
    val media: MediaInfo,
    val gallery: Gallery?,
    //val status: Status
) : VotableData {
    val isCrosspost: Boolean
        get() {
            return crosspostParentList.isNotEmpty()
        }
}