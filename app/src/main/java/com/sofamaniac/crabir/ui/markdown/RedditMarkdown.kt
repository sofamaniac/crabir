/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.mikepenz.markdown.annotator.AnnotatorSettings
import com.mikepenz.markdown.annotator.DefaultAnnotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.DefaultMarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.ReferenceLinkHandler
import com.mikepenz.markdown.model.ReferenceLinkHandlerImpl
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.markdownAnimations
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.settings.theme.CrabirTheme
import com.sofamaniac.crabir.ui.markdown.redditFlavour.RedditFlavourDescriptor
import com.sofamaniac.crabir.ui.markdown.redditFlavour.RedditFlavourElementType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.core.MarkwonTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode


@Composable
fun InnerRedditMarkdown(
    markdown: String,
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
        animations = markdownAnimations(animateTextSize = { this.fillMaxSize() }),
        components = markdownComponents { type, model ->
            Log.d("CustomComponents", "$type")
            if (type == RedditFlavourElementType.SPOILER) {
                Text(
                    model.content,
                    modifier = Modifier.background(Color.Red)
                )
            }
        },
        annotator = SpoilerAnnotator(
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
        InnerRedditMarkdown(markdown, modifier, viewModel)
    } else {
        HeightRestrictedWithGradient(
            maxHeight = with(LocalDensity.current) { (MaterialTheme.typography.bodyMedium.lineHeight * maxLines).toDp() },
            modifier = modifier
        ) {
            InnerRedditMarkdown(markdown, modifier, viewModel)
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

private fun String.convertRedditSuperscript(): String {
    // Convert reddit superscript to tag based superscript
    val redditSuperscriptPattern = Regex(
        """\^\(([^)]+)\)|\^\^(\S+)"""
    )

    return redditSuperscriptPattern.replace(this) { matchResult ->
        "<sup>${
            matchResult.groupValues[1].ifEmpty { matchResult.groupValues[2] }
        }</sup > "
    }
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
        "[https://giphy.com/gifs/${id}](https://giphy.com/gifs/${id})"
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
            //.convertGiphy()
            //.convertRedditSpoilers()
            .convertRedditPreviewLinks(mediaMetadata)
    //.convertRedditSuperscript()
    //.fuseQuote()


    val markdownFlow = parseMarkdownFlow(processedMarkdown, flavour = RedditFlavourDescriptor())
        .stateIn(
            viewModelScope, SharingStarted.Eagerly, State.Loading()
        )
    private var _height by mutableStateOf<Int?>(null)
    val height = _height
    fun reset() {
        _height = null
    }

    fun setHeight(height: Int) {
        _height = height
    }

    @AssistedFactory
    interface Factory {
        fun create(markdown: String, mediaMetadata: Map<String, MediaMetadata>): MarkdownViewModel
    }
}

class MarkdownTheme(val colorScheme: ColorScheme, val theme: CrabirTheme) :
    AbstractMarkwonPlugin() {
    override fun configureTheme(builder: MarkwonTheme.Builder) {
        builder
            .linkColor(theme.linkColor.toArgb())
            .codeTextColor(colorScheme.onBackground.toArgb())
            .codeBackgroundColor(colorScheme.background.toArgb())
            .codeBlockBackgroundColor(colorScheme.background.toArgb())
            .blockQuoteColor(theme.highlight.toArgb())
            // Disable ruler under titles ?
            .headingBreakColor(colorScheme.background.toArgb())
    }
}

class NestedSpoilerAnnotator(
    typography: MarkdownTypography,
    referenceLinkHandler: ReferenceLinkHandler,
    override var config: MarkdownAnnotatorConfig
) : MarkdownAnnotator {
    val settings = DefaultAnnotatorSettings(
        linkTextSpanStyle = typography.textLink,
        codeSpanStyle = typography.inlineCode.toSpanStyle(),
        annotator = this,
        referenceLinkHandler = referenceLinkHandler,
    )
    override val annotate: (AnnotatedString.Builder.(content: String, child: ASTNode) -> Boolean) =
        { content, child ->
            // Ignores nested spoilers
            if (child.type == RedditFlavourElementType.SPOILER) {
                buildMarkdownAnnotatedString(
                    content,
                    child.children.removeSpoilerMarker(),
                    settings
                )
                true
            } else {
                false
            }
        }
}

class SpoilerAnnotator(
    typography: MarkdownTypography,
    referenceLinkHandler: ReferenceLinkHandler,
    val spoilers: MutableMap<String, Boolean>,
    override var config: MarkdownAnnotatorConfig
) : MarkdownAnnotator {

    val makeSettings: (text: String) -> AnnotatorSettings = { text ->
        DefaultAnnotatorSettings(
            linkTextSpanStyle = typography.textLink,
            codeSpanStyle = typography.inlineCode.toSpanStyle(),
            annotator = NestedSpoilerAnnotator(typography, referenceLinkHandler, config),
            referenceLinkHandler = referenceLinkHandler,
            linkInteractionListener = if (spoilers[text] == true) {
                null
            } else {
                { spoilers[text] = true }
            }
        )

    }
    override val annotate: (AnnotatedString.Builder.(content: String, child: ASTNode) -> Boolean) =
        { content, child ->
            val start = child.startOffset
            val end = child.endOffset.coerceAtMost(content.length)
            val text = content.substring(start until end)
            if (child.type == RedditFlavourElementType.SPOILER) {
                //appendInlineContent("SPOILER", child.getUnescapedTextInNode(content))
                pushStringAnnotation(tag = "SPOILER", annotation = text)
                withStyle(
                    SpanStyle(
                        color = if (spoilers[text] == true) Color.White else Color.Transparent,
                        background = if (spoilers[text] == true) Color.Unspecified else Color.White
                    )
                ) {
                    addLink(
                        LinkAnnotation.Clickable(tag = "SPOILER", linkInteractionListener = {
                            spoilers[text] = true
                        }),
                        0,
                        text.length
                    )
                    Log.d("SpoilerAnnotator", "${child.children}")
                    buildMarkdownAnnotatedString(
                        content,
                        child.children.removeSpoilerMarker(),
                        makeSettings(text)
                    )
                }
                pop()
                true
            } else {
                false
            }
        }
}

internal fun List<ASTNode>.removeSpoilerMarker(): List<ASTNode> {

    val start =
        if (this.firstOrNull()?.type == RedditFlavourElementType.SPOILER_START) {
            1
        } else if (this.getOrNull(0)?.type == MarkdownTokenTypes.GT
            && this.getOrNull(1)?.type == MarkdownTokenTypes.EXCLAMATION_MARK
        ) {
            2
        } else {
            0
        }
    val end =
        if (this.getOrNull(size - 1)?.type == MarkdownTokenTypes.LT
            && this.getOrNull(size - 2)?.type == MarkdownTokenTypes.EXCLAMATION_MARK
        ) {
            size - 2
        } else {
            size
        }
    return this.subList(start, end)
}