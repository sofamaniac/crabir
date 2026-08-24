package com.sofamaniac.crabir.domain.model

import androidx.core.net.toUri
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ParsedMarkdown private constructor(val markdown: String) {
    constructor(rawMarkdown: String, mediaMetadata: Map<String, MediaMetadata> = emptyMap()) : this(
        rawMarkdown
//.extractRedditLinks()
            //.convertRedditVideoLink()
            .convertGiphy()
            .convertRedditPreviewLinks(mediaMetadata)
    )
}

/** Convert links of the form `https://reddit.com/link/[POSTID]/video/[VIDEOID]/player to a direct to the video. */
private fun String.convertRedditVideoLink(): String {
    val redditLinkPattern = Regex("https://reddit\\.com/link/\\w+/video/(\\w+)/player")
    return redditLinkPattern.replace(this) { matchResult ->
        "![\uE000](https://v.redd.it/${matchResult.groupValues[1]})"
    }
}

/** Convert all Markdown links that correspond to some media metadata to a Markdown image */
private fun String.convertRedditPreviewLinks(mediaMetadata: Map<String, MediaMetadata>): String {
    val redditPreviewPatternAltText = Regex(
        """\[(.*)]\((https://preview\.redd\.it/[^\s)]+)\)"""
    )
    val redditPreviewPattern = Regex("(?<!\\S)(https://preview\\.redd\\.it/[^\\s)]+)")

    val s = redditPreviewPatternAltText.replace(this) { matchResult ->
        val altText = matchResult.groupValues[1]
        val url = matchResult.groupValues[2]

        val filename = url.toUri().lastPathSegment?.split('.')?.first()

        val metadata = mediaMetadata[filename]
        if (metadata != null) {
            val foundUrl = metadata.toMediaResource()?.url
            if (foundUrl != null) {
                "![$altText]($foundUrl)"
            } else {
                "![$altText]($url)"
            }
        } else {
            matchResult.value
        }
    }
    val res = redditPreviewPattern.replace(s) { matchResult ->
        "![${matchResult.groupValues[1]}](${matchResult.groupValues[1]})"
    }
    return res
}


/**
 * Convert reddit relative links (r/..., u/...) to full links
 */
private fun String.extractRedditLinks(): String {
    val redditLinksPattern = Regex("(\\p{Punct}|\\s)?/?([ru]/\\w{2,24}/?)")
    val res = redditLinksPattern.replace(this) { matchResult ->
        val prefix = matchResult.groupValues[1]
        val dest = matchResult.groupValues[2]
        "$prefix[$dest](https://www.reddit.com/$dest)"
    }
    return res
}

/** Convert embedded giphy GIFs to Markdown links / images
 * @param toImage when set to true convert GIFs to Markdown images otherwise convert to link
 */
private fun String.convertGiphy(): String {
    val giphyPatter = Regex("!\\[gif]\\(giphy\\|(.*)\\)")
    return giphyPatter.replace(this) { matchResult ->
        val id = matchResult.groupValues[1].split("|").first()
        "![https://giphy.com/gifs/${id}](https://media.giphy.com/media/${id}/giphy.gif)"
    }
}
