/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.markdown

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.ui.markdown.redditFlavour.RedditFlavourDescriptor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


@Composable
fun InnerRedditMarkdown(
    modifier: Modifier = Modifier,
    viewModel: MarkdownViewModel,
) {
    val state by viewModel.markdownFlow.collectAsStateWithLifecycle()
    val typography = markdownTypography()
    val referenceLinkHandler = ReferenceLinkHandlerImpl()
    val spoilers = remember { mutableStateMapOf<String, Boolean>() }
    Markdown(
        state,
        modifier = modifier,
        imageTransformer = Coil3ImageTransformerImpl,
        // Disable animations
        animations = markdownAnimations(animateTextSize = { this }),
        components = markdownComponents(inlineImage = { model ->
            ClickableMarkdownInlineImage(model.content, model.node)
        }),
        annotator = RedditAnnotator(
            typography, referenceLinkHandler, spoilers,
            DefaultMarkdownAnnotatorConfig()
        ),
    )
}

@Composable
fun RedditMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    key: String? = markdown,
    viewModel: MarkdownViewModel = hiltViewModel<MarkdownViewModel, MarkdownViewModel.Factory>(key = key) { factory ->
        factory.create(markdown, mediaMetadata)
    }
) {
    if (maxLines == Int.MAX_VALUE) {
        InnerRedditMarkdown(modifier, viewModel = viewModel)
    } else {
        HeightRestrictedWithGradient(
            maxHeight = with(LocalDensity.current) { (MaterialTheme.typography.bodyMedium.lineHeight * maxLines).toDp() },
        ) {
            InnerRedditMarkdown(modifier, viewModel)
        }
    }
}


/** Convert all markdown links that correspond to some media metadata to a markdown image */
private fun String.convertRedditPreviewLinks(mediaMetadata: Map<String, MediaMetadata>): String {
    val redditPreviewPatternAltText = Regex(
        """\[(.*)]\((https://preview\.redd\.it/[^\s)]+)\)"""
    )
    val redditPreviewPattern = Regex("(?<!\\S)(https://preview\\.redd\\.it/[^\\s)]+)")

    val s = redditPreviewPatternAltText.replace(this) { matchResult ->
        val alttext = matchResult.groupValues[1]
        val url = matchResult.groupValues[2]

        val filename = url.toUri().lastPathSegment?.split('.')?.first()

        val metadata = mediaMetadata[filename]
        if (metadata != null) {
            val foundUrl = metadata.toMediaResource()?.url
            if (foundUrl != null) {
                "![$alttext]($foundUrl)"
            } else {
                "![$alttext]($url)"
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


private fun String.extractRedditLinks(): String {
    val redditLinksPattern = Regex("(?<!\\S)/?([ru]/[A-Za-z0-9_-]+/?)")
    val res = redditLinksPattern.replace(this) { matchResult ->
        "[${matchResult.value}](https://www.reddit.com/${matchResult.value})"
    }
    return res
}

private fun String.fuseQuote(): String {
    val quotePattern = Regex(">(.*)\n(\n+)>")
    return quotePattern.replace(this) { matchResult ->
        val newLines = ">\n".repeat(matchResult.groupValues[2].length)
        ">${matchResult.groupValues[1]}\n$newLines>"
    }
}

private fun String.convertGiphy(): String {
    val giphyPatter = Regex("!\\[gif]\\(giphy\\|(.*)\\)")
    return giphyPatter.replace(this) { matchResult ->
        val id = matchResult.groupValues[1].split("|").first()
        "![https://giphy.com/gifs/${id}](https://media.giphy.com/media/${id}/giphy.gif)"
    }
}

@HiltViewModel(assistedFactory = MarkdownViewModel.Factory::class)
class MarkdownViewModel @AssistedInject constructor(
    @Assisted val markdown: String,
    @Assisted val mediaMetadata: Map<String, MediaMetadata>
) : ViewModel() {
    val processedMarkdown =
        markdown
            .extractRedditLinks()
            .convertGiphy()
            //.convertRedditSpoilers()
            .convertRedditPreviewLinks(mediaMetadata)
    //.convertRedditSuperscript()
    //.fuseQuote()


    val markdownFlow = parseMarkdownFlow(processedMarkdown, flavour = RedditFlavourDescriptor())
        .stateIn(
            viewModelScope, SharingStarted.Eagerly, State.Loading()
        )

    @AssistedFactory
    interface Factory {
        fun create(markdown: String, mediaMetadata: Map<String, MediaMetadata>): MarkdownViewModel
    }
}