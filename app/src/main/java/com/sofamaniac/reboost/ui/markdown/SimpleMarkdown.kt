/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.markdown

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin


@Composable
fun SimpleMarkdown(markdown: String, modifier: Modifier = Modifier, maxLines: Int = Int.MAX_VALUE) {
    val colorScheme = MaterialTheme.colorScheme

    val processedMarkdown = markdown.replace(">!", "¤¤").replace("!<", "¤¤")

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
    val markwon = Markwon.builder(context)
        .usePlugin(MarkwonInlineParserPlugin.create())
        .useRedditSpoilers()
        //.usePlugin(HtmlPlugin.create())
        .usePlugin(TablePlugin.create(context))
        //.usePlugin(CoilImagesPlugin.create())
        .usePlugin(MarkdownTheme())
//        .usePlugin(SimpleExtPlugin.create { plugin ->
//            plugin.addExtension(2, '¤', SpanFactory { _, _ ->
//                SpoilerSpan()
//            })
//        })
        .build()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setTextColor(colorScheme.onBackground.toArgb())
                setLinkTextColor(colorScheme.primary.toArgb())
                movementMethod = LinkMovementMethod.getInstance()
                this.maxLines = maxLines
            }
        },
        modifier = modifier,
        update = { textView ->
            markwon.setMarkdown(textView, processedMarkdown)
            // Disable link when truncating view and allow clicks to be passed to parent view.
            if (maxLines != Int.MAX_VALUE) {
                textView.movementMethod = null
            }
        }
    )
}

