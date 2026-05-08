/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.markdown

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.MediaResource
import com.sofamaniac.crabir.settings.theme.CrabirTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import jakarta.inject.Inject


/** Display Reddit style markdown.
 * @param markdown The markdown to display.
 * @param modifier The modifier to apply to the text.
 * @param maxLines The maximum number of lines to display.
 *
 * If [maxLines] is different from [Int.MAX_VALUE], the link in the text will not be clickable
 */
@Composable
fun RedditMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    key: String? = markdown,
    viewModel: MarkdownViewModel = hiltViewModel(key = key)
) {
    val colorScheme = MaterialTheme.colorScheme


    val context = LocalContext.current

    val processedMarkdown = remember(context) {
        markdown
            .extractRedditLinks()
            .convertGiphy()
            //.convertRedditSpoilers()
            //.convertRedditPreviewLinks(mediaMetadata)
            .convertRedditSuperscript()
            .fuseQuote()
    }

    val theme = LocalTheme.current
    val markwonReddit = remember(redditMarkwonBuilder(context, colorScheme, theme, mediaMetadata))
    // Parse markdown once and remember it
    val parsedMarkdown = remember(processedMarkdown, markwonReddit) {
        markwonReddit.parse(processedMarkdown)
    }

    val spanned = remember(parsedMarkdown, markwonReddit, maxLines) {
        markwonReddit.render(parsedMarkdown)
    }
    LaunchedEffect(maxLines) {
        viewModel.reset()
    }

    key(maxLines) {
        AndroidView(
            factory = { ctx ->
                initTextView(
                    ctx,
                    theme,
                    maxLines,
                    markdown,
                    markwonReddit,
                    spanned
                )
            },
            modifier = modifier
                .onSizeChanged { size ->
                    if (size.height > (viewModel.height ?: 0)) {
                        viewModel.setHeight(size.height)
                    }
                }
                .let { mod ->
                    viewModel.height?.let { size ->
                        mod.height(with(LocalDensity.current) { size.toDp() })
                    } ?: mod
                }
                .fillMaxWidth(),
            update = { textView ->
                //markwonReddit.setParsedMarkdown(textView, spanned)
                if (textView.maxLines != maxLines) {
                    textView.maxLines = maxLines
                    textView.invalidate()
                }
                // Disable link when truncating view and allow clicks to be passed to parent view.
                if (maxLines != Int.MAX_VALUE) {
                    textView.movementMethod = null
                    textView.ellipsize = TextUtils.TruncateAt.END
                } else {
                    textView.ellipsize = null
                    textView.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        )
    }
}


fun initTextView(
    ctx: Context,
    colorScheme: CrabirTheme,
    maxLines: Int,
    markdown: String,
    markwonReddit: Markwon,
    spanned: Spanned
): TextView {
    val contextWrapper =
        ContextThemeWrapper(ctx, androidx.appcompat.R.style.Theme_AppCompat)
    val textView = PassThroughTextView(contextWrapper)

    return textView.apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setTextColor(colorScheme.contentColor.toArgb())
        this.maxLines = maxLines
        movementMethod = if (maxLines == Int.MAX_VALUE)
            LinkMovementMethod.getInstance() else null
        if (maxLines != Int.MAX_VALUE)
            ellipsize = TextUtils.TruncateAt.END
        markwonReddit.setParsedMarkdown(this, spanned)
        tag = markdown

        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    val max = textView.maxLines.coerceAtLeast(1)
                    val layout: Layout? = textView.layout
                    if ((layout?.lineCount ?: 0) > max) {
                        val end = layout!!.getLineEnd(max - 1)
                        val text = textView.text
                        textView.setText(
                            text.subSequence(
                                0,
                                (end - 3).coerceAtLeast(0)
                            ),
                            TextView.BufferType.SPANNABLE
                        )
                        textView.append("...")
                    }
                }
            }
        )
    }
}

@Composable
private fun redditMarkwonBuilder(
    context: Context,
    colorScheme: ColorScheme,
    theme: CrabirTheme,
    mediaMetadata: Map<String, MediaMetadata>
): () -> Markwon = {

    Markwon.builder(context)
        .useRedditSpoilers()
        .usePlugin(MarkwonInlineParserPlugin.create())
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(MarkdownTheme(colorScheme, theme))
        .usePlugin(HtmlPlugin.create())
        .usePlugin(LinkifyPlugin.create(Linkify.WEB_URLS))
        .usePlugin(
            GlideImagesPlugin.create(
                object : GlideImagesPlugin.GlideStore {

                    private val requestManager = Glide.with(context).apply {
                        addDefaultRequestListener(object : RequestListener<Any> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Any>,
                                isFirstResource: Boolean
                            ): Boolean = false

                            override fun onResourceReady(
                                resource: Any,
                                model: Any,
                                target: Target<Any>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                (resource as? GifDrawable)?.start()
                                return false
                            }
                        })
                    }


                    override fun load(drawable: AsyncDrawable): RequestBuilder<Drawable?> {
                        val metadata = drawable.getMetadata(mediaMetadata)
                        val placeholder =
                            Color.GRAY.toDrawable()
                        if (metadata != null) {
                            placeholder.setBounds(
                                0,
                                0,
                                metadata.width,
                                metadata.height
                            )
                        }

                        return requestManager
                            .load(metadata?.url ?: drawable.destination)
                    }

                    override fun cancel(target: Target<*>) {
                        requestManager.clear(target)
                    }
                }

            ))
        //.usePlugin(FullWidthImagePlugin(mediaMetadata))
        .build()
}

private fun AsyncDrawable.getMetadata(mediaMetadata: Map<String, MediaMetadata>): MediaResource? {
    // Handle destination of the form `giphy|xxxx`
    if (destination.contains('|')) {
        val metadata = mediaMetadata[destination]
        if (metadata != null && metadata !is MediaMetadata.Invalid) {
            return metadata.toMediaResource()
        } else {
            val id = destination.split('|').last()
            return MediaResource("https://media.giphy.com/media/$id/giphy.gif", 1f, 100, 100)
        }
    }
    // Otherwise assume destination is a link
    val url = destination.toUri()
    val filename = url.pathSegments.lastOrNull()?.split('.')?.firstOrNull()
    return mediaMetadata[filename]?.toMediaResource()
}

//private fun String.convertRedditSpoilers(): String {
//    val spoilerRegex = Regex(""">!(.*?)!<""")
//    return spoilerRegex.replace(this) {
//        val inner = it.groupValues[1]
//        "$SPOILER_OPEN $inner $SPOILER_CLOSE;"
//    }
//}
//

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
            "![$alttext]($url)"
        } else {
            matchResult.value
        }
    }
    return redditPreviewPattern.replace(s) { matchResult ->
        "![Preview](${matchResult.groupValues[1]})"
    }
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

@HiltViewModel
class MarkdownViewModel @Inject constructor() : ViewModel() {
    private var _height by mutableStateOf<Int?>(null)
    val height = _height
    fun reset() {
        _height = null
    }

    fun setHeight(height: Int) {
        _height = height
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