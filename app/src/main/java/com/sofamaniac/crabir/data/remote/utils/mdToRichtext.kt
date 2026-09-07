package com.sofamaniac.crabir.data.remote.utils

import android.util.Log
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.redditmarkdown.redditFlavour.RedditFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

object RedditMarkdownParser {
    val parser = MarkdownParser(RedditFlavourDescriptor(true))
}

fun RichtextDocument.Companion.fromMd(
    markdown: ParsedMarkdown,
    enableImages: Boolean = true,
): RichtextDocument {
    val tree =
        RedditMarkdownParser.parser.buildMarkdownTreeFromString(markdown.markdown as CharSequence)
    val html =
        HtmlGenerator(
            markdown.markdown,
            tree,
            RedditFlavourDescriptor(enableImages)
        ).generateHtml()
            .replace("<body>", "<div class=\"md\">")
            .replace("</body>", "</div>")
    Log.d("Richtext", "Html: $html")
    return RichtextDocument.fromHtml(html)
}
