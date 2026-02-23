/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.markdown

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.MediaResource
import com.sofamaniac.reboost.settings.DefaultReboostTheme
import com.sofamaniac.reboost.settings.ReboostTheme
import com.sofamaniac.reboost.settings.themeDataStore
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


    val processedMarkdown = markdown
        .convertRedditSpoilers()
        .convertRedditPreviewLinks()
        .convertRedditSuperscript()


    val context = LocalContext.current
    val textView = remember { PassThroughTextView(context) }

    // Fix missing ellipsis https://github.com/noties/Markwon/issues/180
    textView.getViewTreeObserver()
        .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                val max = textView.maxLines
                val layout: Layout? = textView.layout
                if ((layout?.lineCount ?: 0) > max) {
                    val end = layout!!.getLineEnd(max - 1);
                    textView.setText(
                        textView.getText().subSequence(0, end - 3),
                        TextView.BufferType.SPANNABLE
                    );
                    textView.append("...");
                }
            }
        });

    val coroutineScope = rememberCoroutineScope()
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme,
        coroutineScope.coroutineContext
    )
    val markwonReddit = remember(redditMarkwonBuilder(context, colorScheme, theme, mediaMetadata))
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
                //this.movementMethod = LinkTouchMovementMethod.getInstance()
                this.maxLines = maxLines
                markwonReddit.setParsedMarkdown(textView, spanned)

                // Disable link when truncating view and allow clicks to be passed to parent view.
                if (maxLines != Int.MAX_VALUE) {
                    textView.movementMethod = null
                    textView.ellipsize = TextUtils.TruncateAt.END
                } else {
                    textView.movementMethod = LinkMovementMethod.getInstance()
                }
                textView.tag = markdown
            }
        },
        modifier = modifier
            .onSizeChanged { size ->
                viewModel.height = size.height
            }
            .let { mod ->
                if (maxLines != Int.MAX_VALUE) {
                    return@let mod
                }
                viewModel.height?.let { size ->
                    mod.then(Modifier.height(with(LocalDensity.current) { size.toDp() }))
                } ?: mod
            },
        update = { textView ->
            if (textView.tag != markdown) {
                textView.tag = markdown
                markwonReddit.setParsedMarkdown(textView, spanned)
                // Disable link when truncating view and allow clicks to be passed to parent view.
                if (maxLines != Int.MAX_VALUE) {
                    textView.movementMethod = null
                } else {
                    textView.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    )
}

@Composable
private fun redditMarkwonBuilder(
    context: Context,
    colorScheme: ColorScheme,
    theme: ReboostTheme,
    mediaMetadata: Map<String, MediaMetadata>
): () -> Markwon = {

    Markwon.builder(context)
        .usePlugin(MarkwonInlineParserPlugin.create())
        .usePlugin(StrikethroughPlugin.create())
        .useRedditSpoilers()
        .usePlugin(HtmlPlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(MarkdownTheme(colorScheme, theme))
        .usePlugin(HtmlPlugin.create())
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
                        placeholder.setBounds(0, 0, metadata?.width ?: 0, metadata?.height ?: 0)

                        return requestManager
                            .load(metadata?.url ?: drawable.destination)
                            .placeholder(
                                placeholder
                            )
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
        Log.d("SimpleMarkdown", "Handling destination $destination")
        Log.d("SimpleMarkdown", "Media metadata ${mediaMetadata[destination]}")
        val metadata = mediaMetadata[destination]
        if (metadata != null && metadata !is MediaMetadata.Invalid) {
            return metadata.toMediaResource()
        } else {
            val filename = destination.split('|').last()
            return MediaResource("https://media.giphy.com/media/$filename/giphy.gif", 1f, 0, 0)
        }
    }
    // Otherwise assume destination is a link
    val url = destination.toUri()
    val filename = url.pathSegments.last().split('.').first()
    return mediaMetadata[filename]?.toMediaResource()
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

private fun String.convertRedditSuperscript(): String {
    // Match Reddit preview links that aren't already in markdown syntax
    val redditSuperscriptPattern = Regex(
        """\^\(([^)]+)\)|\^\^(\S+)"""
    )

    return redditSuperscriptPattern.replace(this) { matchResult ->
        "<sup>${
            matchResult.groupValues[1].ifEmpty { matchResult.groupValues[2] }
        }</sup > "
    }
}

@HiltViewModel
class MarkdownViewModel @Inject constructor() : ViewModel() {
    var height: Int? = null

    init {
        height = null
    }
}

class MarkdownTheme(val colorScheme: ColorScheme, val theme: ReboostTheme) :
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
