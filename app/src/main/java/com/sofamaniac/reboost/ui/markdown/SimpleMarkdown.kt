/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.markdown

import android.graphics.Rect
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.ImageSize
import io.noties.markwon.image.ImageSizeResolverDef
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin


@Composable
        /** Display Reddit style markdown.
         * @param markdown The markdown to display.
         * @param modifier The modifier to apply to the text.
         * @param maxLines The maximum number of lines to display.
         *
         * If [maxLines] is different from [Int.MAX_VALUE], the link in the text will not be clickable
         */
fun SimpleMarkdown(markdown: String, modifier: Modifier = Modifier, maxLines: Int = Int.MAX_VALUE) {
    val colorScheme = MaterialTheme.colorScheme

    val processedSpoiler = markdown.replace(">!", "\ue000").replace("!<", "\ue000")
    val processedMarkdown = convertRedditPreviewLinks(processedSpoiler)

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
    val markwonReddit = Markwon.builder(context)
        .usePlugin(MarkwonInlineParserPlugin.create())
        .useRedditSpoilers()
        .usePlugin(TablePlugin.create(context))
        .usePlugin(MarkdownTheme())
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                builder.imageSizeResolver(object : ImageSizeResolverDef() {
                    override fun resolveImageSize(
                        imageSize: ImageSize?,
                        imageBounds: Rect,
                        canvasWidth: Int,
                        textSize: Float
                    ): Rect {
                        return if (imageSize == null) {
                            fitWidth(imageBounds, canvasWidth)
                        } else {
                            super.resolveImageSize(imageSize, imageBounds, canvasWidth, textSize)
                        }
                    }

                    fun fitWidth(imageBounds: Rect, canvasWidth: Int): Rect {
                        val ratio = imageBounds.width().toFloat() / imageBounds.height().toFloat()
                        val newHeight = (canvasWidth / ratio).toInt()
                        return Rect(0, 0, canvasWidth, newHeight)
                    }
                });
            }
        })
        .usePlugin(ImagesPlugin.create())

        .build()

    val textView = TextView(context)

    AndroidView(
        factory = { context ->
            textView.apply {
                setTextColor(colorScheme.onBackground.toArgb())
                setLinkTextColor(colorScheme.primary.toArgb())
                movementMethod = LinkTouchMovementMethod.getInstance()
                this.maxLines = maxLines
            }
        },
        modifier = Modifier.pointerInteropFilter { event ->
            LinkTouchMovementMethod.getInstance().onTouchEvent(
                textView,
                textView.text as? Spannable ?: return@pointerInteropFilter false,
                event
            )
        },
        update = { textView ->
            markwonReddit.setMarkdown(textView, processedMarkdown)
            // Disable link when truncating view and allow clicks to be passed to parent view.
            if (maxLines != Int.MAX_VALUE) {
                textView.movementMethod = null
            } else {
                textView.movementMethod = LinkTouchMovementMethod.getInstance()
            }
            //textView.text = processedMarkdown
            //textView.movementMethod = null
        }
    )
}

// Code produced by Claude
class LinkTouchMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            // Check for URLSpan specifically (actual links), not all ClickableSpans
            val links = buffer.getSpans(off, off, ClickableSpan::class.java)

            if (links.isNotEmpty()) {
                return super.onTouchEvent(widget, buffer, event)
            }
        }

        // No link at touch position, don't consume the event
        return false
    }

    companion object {
        private var sInstance: LinkTouchMovementMethod? = null

        fun getInstance(): MovementMethod {
            if (sInstance == null) {
                sInstance = LinkTouchMovementMethod()
            }
            return sInstance!!
        }
    }
}

fun convertRedditPreviewLinks(markdown: String): String {
    // Match Reddit preview links that aren't already in markdown syntax
    val redditPreviewPattern = Regex(
        """(?<!]\()https://preview\.redd\.it/[^\s)]+(?!\))"""
    )

    return redditPreviewPattern.replace(markdown) { matchResult ->
        "![](${matchResult.value})"
    }
}