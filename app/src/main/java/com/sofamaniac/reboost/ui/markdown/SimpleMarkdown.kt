/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.markdown

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
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
import com.bumptech.glide.request.target.Target
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import jakarta.inject.Inject


@Composable
        /** Display Reddit style markdown.
         * @param markdown The markdown to display.
         * @param modifier The modifier to apply to the text.
         * @param maxLines The maximum number of lines to display.
         *
         * If [maxLines] is different from [Int.MAX_VALUE], the link in the text will not be clickable
         */
fun SimpleMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    mediaMetadata: Map<String, MediaMetadata> = emptyMap(),
    key: String? = markdown,
    viewModel: MarkdownViewModel = hiltViewModel(key = key)
) {
    val colorScheme = MaterialTheme.colorScheme


    val processedMarkdown = markdown.convertRedditSpoilers().convertRedditPreviewLinks()

    class MarkdownTheme : AbstractMarkwonPlugin() {
        override fun configureTheme(builder: MarkwonTheme.Builder) {
            builder
                .linkColor(colorScheme.primary.toArgb())
                .codeTextColor(colorScheme.onBackground.toArgb())
                .codeBackgroundColor(colorScheme.background.toArgb())
                .codeBlockBackgroundColor(colorScheme.background.toArgb())
                .blockQuoteColor(colorScheme.primary.toArgb())
                // Disable ruler under titles ?
                .headingBreakColor(colorScheme.background.toArgb())
        }
    }

    val context = LocalContext.current
    val textView = remember { TextView(context) }
    val markwonReddit = remember {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .useRedditSpoilers()
            .usePlugin(HtmlPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(MarkdownTheme())
            .usePlugin(
                GlideImagesPlugin.create(
                    object : GlideImagesPlugin.GlideStore {
                        override fun load(drawable: AsyncDrawable): RequestBuilder<Drawable?> {
                            val metadata = drawable.getMetadata(mediaMetadata)
                            val placeholder =
                                Color.GRAY.toDrawable()
                            placeholder.setBounds(0, 0, metadata?.width ?: 0, metadata?.height ?: 0)
                            return Glide.with(context)
                                .load(metadata?.toMediaResource()?.url ?: drawable.destination)
                                .placeholder(
                                    placeholder
                                )
                        }

                        override fun cancel(target: Target<*>) {
                            Glide.with(context).clear(target)
                        }
                    }

                ))
            //.usePlugin(FullWidthImagePlugin(mediaMetadata))
            .build()
    }
    // Parse markdown once and remember it
    val parsedMarkdown = remember(processedMarkdown, markwonReddit) {
        markwonReddit.parse(processedMarkdown)
    }

    val spanned = remember(parsedMarkdown, markwonReddit) {
        markwonReddit.render(parsedMarkdown)
    }
    AndroidView(
//        onReset = {
//            markwonReddit.setParsedMarkdown(textView, spanned)
//            textView.tag = markdown
//        },
        factory = { context ->
            textView.apply {
                setTextColor(colorScheme.onBackground.toArgb())
                setLinkTextColor(colorScheme.primary.toArgb())
                movementMethod = LinkTouchMovementMethod.getInstance()
                this.maxLines = maxLines
                //textView.text = content
                markwonReddit.setMarkdown(textView, processedMarkdown)
                // Disable link when truncating view and allow clicks to be passed to parent view.
                if (maxLines != Int.MAX_VALUE) {
                    textView.movementMethod = null
                } else {
                    textView.movementMethod = LinkTouchMovementMethod.getInstance()
                }
                markwonReddit.setParsedMarkdown(textView, spanned)
                textView.tag = markdown

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        },
        modifier = modifier
            .onSizeChanged { size ->
                viewModel.height = size.height
            }
            .let { mod ->
                viewModel.height?.let { size ->
                    mod.then(Modifier.height(with(LocalDensity.current) { size.toDp() }))
                } ?: mod
            }
            .pointerInteropFilter { event ->
                LinkTouchMovementMethod.getInstance().onTouchEvent(
                    textView,
                    textView.text as? Spannable ?: return@pointerInteropFilter false,
                    event
                )
            },
        update = { textView ->
            //textView.text = content
            if (textView.tag != markdown) {
                textView.tag = markdown
                markwonReddit.setParsedMarkdown(textView, spanned)
            }
        }
    )
}

fun AsyncDrawable.getMetadata(mediaMetadata: Map<String, MediaMetadata>): MediaMetadata? {
    // Handle destination of the form `giphy|xxxx`
    if (destination.contains('|')) {
        return mediaMetadata[destination]
    }
    // Otherwise assume destination is a link
    val url = destination.toUri()
    val filename = url.pathSegments.last().split('.').first()
    return mediaMetadata[filename]
}

private fun String.convertRedditSpoilers(): String {
    return this.replace(">!", " \ue000 ").replace("!<", " \ue000 ")
}

private fun String.convertRedditPreviewLinks(): String {
    // Match Reddit preview links that aren't already in markdown syntax
    val redditPreviewPattern = Regex(
        """(?<!]\()https://preview\.redd\.it/[^\s)]+(?!\))"""
    )

    return redditPreviewPattern.replace(this) { matchResult ->
        //"<img width=\"100%\" src=\"${matchResult.value}\"/>"
        "![](${matchResult.value})"
    }
}

@HiltViewModel
class MarkdownViewModel @Inject constructor() : ViewModel() {
    var height: Int? = null

    init {
        height = null
    }
}