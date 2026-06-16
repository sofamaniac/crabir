/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.DefaultMarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownAnnotator
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.toLocalUrl

@Composable
fun RedditMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int? = null,
    mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    enableImages: Boolean = true,
    key: String? = markdown,
    onClick: LinkInteractionListener? = null,
    enableLinkInteraction: Boolean = true,
) {
    val processedMarkdown = remember(key) {
        markdown
            //.extractRedditLinks()
            .convertGiphy(toImage = enableImages).let {
                if (enableImages)
                    it.convertRedditPreviewLinks(mediaMetadata)
                else
                    it
            }
    }
    val onClick = if (enableLinkInteraction) (onClick ?: redditLinkHandler()) else null
    if (maxLines == null) {
        InnerRedditMarkdown(
            modifier = modifier,
            markdown = processedMarkdown,
            linkInteractionListener = onClick
        )
    } else {
        HeightRestrictedWithGradient(
            modifier = modifier,
            maxHeight = with(LocalDensity.current) { (MaterialTheme.typography.bodyMedium.lineHeight * maxLines).toDp() },
        ) {
            InnerRedditMarkdown(
                markdown = processedMarkdown,
                linkInteractionListener = onClick
            )
        }
    }
}

@Composable
private fun InnerRedditMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    linkInteractionListener: LinkInteractionListener? = null,
) {
    val typography = redditMarkdownTypography()
    val referenceLinkHandler = ReferenceLinkHandlerImpl()
    val spoilers = remember { mutableStateMapOf<String, Boolean>() }
    Markdown(
        content = markdown,
        modifier = modifier,
        typography = typography,
        imageTransformer = Coil3ImageTransformerImpl,
        // Disable animations
        // animations = markdownAnimations(animateTextSize = { this }),
        animations = markdownAnimations(animateTextSize = { Modifier.fillMaxSize() }),
        components = markdownComponents(
            inlineImage = { model ->
                ClickableMarkdownInlineImage(model.content, model.node)
            },
        ),
        annotator = RedditAnnotator(
            typography,
            referenceLinkHandler,
            spoilers,
            config = DefaultMarkdownAnnotatorConfig(),
            linkInteractionListener = linkInteractionListener,
            defaultAnnotator = markdownAnnotator()
        ),
    )
}


@Composable
fun redditLinkHandler(): LinkInteractionListener {
    val navController = LocalNavController.current!!
    val uriHandler = LocalUriHandler.current
    return LinkInteractionListener { link ->
        Log.d("redditLinkHandler", "redditLinkHandler: $link")
        if (link is LinkAnnotation.Url) {
            try {
                Log.d("redditLinkHandler", "navigating to : ${link.url.toLocalUrl()}")
                navController.navigate(link.url.toLocalUrl())
            } catch (e: IllegalArgumentException) {
                Log.i("redditLinkHandler", "failed to open link in app: $e")
                uriHandler.openUri(link.url)
            }
        }
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
        "![Preview](${matchResult.groupValues[1]})"
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
private fun String.convertGiphy(toImage: Boolean): String {
    val giphyPatter = Regex("!\\[gif]\\(giphy\\|(.*)\\)")
    return giphyPatter.replace(this) { matchResult ->
        val id = matchResult.groupValues[1].split("|").first()
        (if (toImage) "!" else "") +
                "[https://giphy.com/gifs/${id}](https://media.giphy.com/media/${id}/giphy.gif)"
    }
}

//@HiltViewModel(assistedFactory = MarkdownViewModel.Factory::class)
//class MarkdownViewModel @AssistedInject constructor(
//    @Assisted val markdown: String,
//    @Assisted val mediaMetadata: Map<String, MediaMetadata>,
//    @Assisted val enableImages: Boolean,
//) : ViewModel() {
//    val processedMarkdown = markdown
//        //.extractRedditLinks()
//        .convertGiphy(toImage = enableImages).let {
//            if (enableImages)
//                it.convertRedditPreviewLinks(mediaMetadata)
//            else
//                it
//        }
//
//    val markdownFlow = parseMarkdownFlow(processedMarkdown, flavour = RedditFlavourDescriptor())
//        .stateIn(
//            viewModelScope, SharingStarted.Eagerly, State.Loading()
//        )
//
//    @AssistedFactory
//    interface Factory {
//        fun create(
//            markdown: String,
//            mediaMetadata: Map<String, MediaMetadata>,
//            enableImages: Boolean
//        ): MarkdownViewModel
//    }
//}

@Composable
fun redditMarkdownTypography(): MarkdownTypography {
    val theme = LocalTheme.current
    val text = MaterialTheme.typography.bodyLarge
    val linkStyle = SpanStyle(
        color = theme.linkColor,
        textDecoration = TextDecoration.Underline
    )
    return markdownTypography(
        h1 = MaterialTheme.typography.headlineLarge,
        h2 = MaterialTheme.typography.headlineMedium,
        h3 = MaterialTheme.typography.headlineSmall,
        h4 = MaterialTheme.typography.titleLarge,
        h5 = MaterialTheme.typography.titleMedium,
        h6 = MaterialTheme.typography.titleSmall,
        text = text,
        code = text.copy(fontFamily = FontFamily.Monospace),
        inlineCode = text.copy(fontFamily = FontFamily.Monospace),
        quote = text.copy(color = theme.highlight)
            .plus(SpanStyle(fontStyle = FontStyle.Italic)),
        paragraph = text,
        ordered = text,
        bullet = text,
        list = text,
        textLink = TextLinkStyles(
            style = linkStyle,
        ),
        table = text,
    )
}
