package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.dto.Thumbnail
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.data.remote.dto.post.Preview
import com.sofamaniac.crabir.data.remote.utils.CommentSortSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Instant

@Serializable
data class PostData(
    override val id: String,
    override val name: Fullname,
    override val relationship: Relationship,
    val createdUtc: Instant,
    val edited: Instant?,
    override val author: AuthorInfo,
    val subreddit: SubredditInfo,
    override val score: Score,
    val url: String,
    val domain: String,
    val permalink: String,
    val title: String,
    @Serializable(with = CommentSortSerializer::class)
    val suggestedSort: Sort?,
    val numComments: Int,
    val over18: Boolean,
    val spoiler: Boolean,
    val sendReplies: Boolean,
    val preview: Preview?,
    val crosspostParentList: List<PostData> = emptyList(),
    val subredditDetails: SubredditData? = null,
    val thumbnail: Thumbnail,
    val selftext: Selftext,
    val mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    val kind: Kind,
    val isDistinguished: Boolean = false,
    val stickied: Boolean = false,
    val archived: Boolean = false,
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

    override val body: ParsedMarkdown = selftext.markdown

    override fun copy(relationship: Relationship?, score: Score?): PostData {
        return copy(relationship = relationship ?: this.relationship, score = score ?: this.score)
    }

    override fun toEntity(): VotableEntity {
        return VotableEntity(
            id = name,
            data = Json.encodeToString(this)
        )
    }
}

val DUMMY_POST = PostData(
    id = "t3_abc123",
    name = Fullname("t3_abc123"),
    relationship = Relationship(
        clicked = false,
        visited = false,
        liked = null,
        saved = false,
        hidden = false
    ),
    createdUtc = Instant.parse("2024-03-15T10:30:00Z"),
    edited = null,
    author = AuthorInfo.DUMMY,
    subreddit = SubredditInfo.DUMMY,
    score = Score(ups = 100, downs = 100, score = 0, upvoteRatio = 1.0, hideScore = false),
    url = "https://google.com",
    domain = "google.com",
    permalink = "/r/DUMMY/comments/abc123/dummy_post_title/",
    title = "Dummy Post Title",
    suggestedSort = null,
    numComments = 42,
    over18 = false,
    spoiler = false,
    sendReplies = true,
    preview = null,
    crosspostParentList = emptyList(),
    subredditDetails = null,
    thumbnail = Thumbnail(
        uri = "https://b.thumbs.redditmedia.com/thumb123.jpg",
        width = 140,
        height = 140,
    ),
    selftext = Selftext.DUMMY,
    mediaMetadata = emptyMap(),
    kind = Kind.Link,
    isDistinguished = false,
    linkFlair = Flair(
        text = "Discussion",
        textColor = "Black",
        backgroundColor = "#ff4500",
        richText = emptyList(),
        type = "text",
    ),
    media = MediaInfo(media = null),
    gallery = null,
    locked = false,
    isCrosspostable = true,
    canModPost = false,
)