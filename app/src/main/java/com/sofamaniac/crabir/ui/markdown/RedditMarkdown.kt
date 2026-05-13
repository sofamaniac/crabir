/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.markdown

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.DefaultMarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.ui.markdown.redditFlavour.RedditFlavourDescriptor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@Composable
fun RedditMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    enableImages: Boolean = true,
    key: String? = markdown,
    viewModel: MarkdownViewModel = hiltViewModel<MarkdownViewModel, MarkdownViewModel.Factory>(key = key) { factory ->
        factory.create(markdown, mediaMetadata, enableImages)
    },
    onClick: (() -> Unit)? = null,
) {
    if (maxLines == Int.MAX_VALUE) {
        InnerRedditMarkdown(modifier, viewModel = viewModel)
    } else {
        HeightRestrictedWithGradient(
            maxHeight = with(LocalDensity.current) { (MaterialTheme.typography.bodyMedium.lineHeight * maxLines).toDp() },
        ) {
            InnerRedditMarkdown(
                modifier,
                viewModel,
                linkInteractionListener = onClick?.let { { onClick() } }
            )
        }
    }
}

@Composable
private fun InnerRedditMarkdown(
    modifier: Modifier = Modifier,
    viewModel: MarkdownViewModel,
    linkInteractionListener: LinkInteractionListener? = null,
) {
    val state by viewModel.markdownFlow.collectAsStateWithLifecycle()
    val typography = redditMarkdownTypography()
    val referenceLinkHandler = ReferenceLinkHandlerImpl()
    val spoilers = remember { mutableStateMapOf<String, Boolean>() }
    Markdown(
        state,
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
    val redditLinksPattern = Regex("(?<!\\S)/?([ru]/[A-Za-z0-9_-]+/?)")
    val res = redditLinksPattern.replace(this) { matchResult ->
        "[${matchResult.value}](https://www.reddit.com/${matchResult.value})"
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

@HiltViewModel(assistedFactory = MarkdownViewModel.Factory::class)
class MarkdownViewModel @AssistedInject constructor(
    @Assisted val markdown: String,
    @Assisted val mediaMetadata: Map<String, MediaMetadata>,
    @Assisted val enableImages: Boolean,
) : ViewModel() {
    val processedMarkdown = markdown
        .extractRedditLinks()
        .convertGiphy(toImage = enableImages).let {
            if (enableImages)
                it.convertRedditPreviewLinks(mediaMetadata)
            else
                it
        }

    val markdownFlow = parseMarkdownFlow(processedMarkdown, flavour = RedditFlavourDescriptor())
        .stateIn(
            viewModelScope, SharingStarted.Eagerly, State.Loading()
        )

    @AssistedFactory
    interface Factory {
        fun create(
            markdown: String,
            mediaMetadata: Map<String, MediaMetadata>,
            enableImages: Boolean
        ): MarkdownViewModel
    }
}

@Composable
fun redditMarkdownTypography(): MarkdownTypography {
    val theme = LocalTheme.current
    val text = MaterialTheme.typography.bodyLarge
    val linkStyle = MaterialTheme.typography.bodyMediumEmphasized.copy(
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
            style = linkStyle.toSpanStyle(),
            pressedStyle = linkStyle.copy(color = Color(0xFF800080)).toSpanStyle()
        ),
        table = text,
    )
}
