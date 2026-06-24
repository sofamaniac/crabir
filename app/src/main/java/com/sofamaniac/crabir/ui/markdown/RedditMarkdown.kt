/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
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
import com.mikepenz.markdown.model.rememberMarkdownState
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.toLocalUrl
import com.sofamaniac.redditmarkdown.redditFlavour.RedditFlavourDescriptor

@Composable
fun RedditMarkdown(
    markdown: State,
    modifier: Modifier = Modifier,
    maxLines: Int? = null,
    enableImages: Boolean = true,
    key: Any,
    spoilerState: SpoilerState = rememberSpoilerState(),
) {
    HeightRestrictedMarkdown(markdown, modifier, maxLines, enableImages, key, spoilerState)
}

@Composable
fun RedditMarkdown(
    markdown: ParsedMarkdown,
    modifier: Modifier = Modifier,
    maxLines: Int? = null,
    enableImages: Boolean = true,
    key: Any? = null,
    spoilerState: SpoilerState = rememberSpoilerState(),
) {
    val markdownState = rememberMarkdownState(
        markdown.markdown,
        flavour = RedditFlavourDescriptor(enableImages),
        retainState = true
    )
    val state by markdownState.state.collectAsState()
    HeightRestrictedMarkdown(
        state,
        modifier,
        maxLines,
        enableImages,
        key = key,
        spoilers = spoilerState
    )
}

@Composable
private fun HeightRestrictedMarkdown(
    markdown: State,
    modifier: Modifier = Modifier,
    maxLines: Int? = null,
    enableImages: Boolean,
    key: Any? = null,
    spoilers: SpoilerState,
) {
    val onClick = if (maxLines == null) redditLinkHandler() else LinkInteractionListener { }
    if (maxLines == null) {
        InnerRedditMarkdown(
            modifier = modifier,
            markdown = markdown,
            enableImages = enableImages,
            linkInteractionListener = onClick,
            key = key,
            spoilers = spoilers,
        )
    } else {
        HeightRestrictedWithGradient(
            modifier = modifier,
            maxHeight = with(LocalDensity.current) { (MaterialTheme.typography.bodyMedium.lineHeight * maxLines).toDp() },
        ) {
            InnerRedditMarkdown(
                markdown = markdown,
                linkInteractionListener = onClick,
                enableImages = enableImages,
                key = key,
                spoilers = spoilers,
            )
        }
    }
}

@Composable
private fun InnerRedditMarkdown(
    markdown: State,
    modifier: Modifier = Modifier,
    linkInteractionListener: LinkInteractionListener,
    enableImages: Boolean,
    key: Any? = null,
    spoilers: SpoilerState,
) {
    val typography = redditMarkdownTypography()
    val referenceLinkHandler = ReferenceLinkHandlerImpl()
    CompositionLocalProvider(LocalUriHandler provides redditUriHandler()) {
        Markdown(
            markdown,
            modifier = modifier,
            typography = typography,
            imageTransformer = Coil3ImageTransformerImpl,
            // Disable animations
            // animations = markdownAnimations(animateTextSize = { this }),
            animations = markdownAnimations(animateTextSize = { Modifier.fillMaxSize() }),
            components = markdownComponents(
                inlineImage = { model ->
                    ClickableMarkdownInlineImage(model.content, model.node, linkInteractionListener)
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
                //uriHandler.openUri(link.url)
            } catch (e: IllegalArgumentException) {
                Log.i("redditLinkHandler", "failed to open link in app: $e")
                uriHandler.openUri(link.url)
            }
        }
    }
}

@Composable
fun redditUriHandler(): UriHandler {
    val navController = LocalNavController.current!!
    val uriHandler = LocalUriHandler.current
    return object : UriHandler {
        override fun openUri(uri: String) {
            try {
                Log.d("redditUriHandler", "navigating to : ${uri.toLocalUrl()}")
                navController.navigate(uri.toLocalUrl())
                //uriHandler.openUri(link.url)
            } catch (e: IllegalArgumentException) {
                Log.i("redditUriHandler", "failed to open link in app: $e")
                uriHandler.openUri(uri)
            }
        }
    }
}


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
