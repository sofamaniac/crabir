package com.sofamaniac.crabir.ui.markdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.annotator.AnnotatorSettings
import com.mikepenz.markdown.annotator.DefaultAnnotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.ReferenceLinkHandler
import com.mikepenz.markdown.utils.getUnescapedTextInNode
import com.sofamaniac.crabir.ui.markdown.redditFlavour.RedditFlavourElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getParentOfType

class RedditAnnotator(
    typography: MarkdownTypography,
    referenceLinkHandler: ReferenceLinkHandler,
    val spoilers: MutableMap<String, Boolean>,
    override var config: MarkdownAnnotatorConfig,
    val depth: Int = 0,
    val linkInteractionListener: LinkInteractionListener?,
    val defaultAnnotator: MarkdownAnnotator,
) : MarkdownAnnotator {

    val superscriptStyle =
        SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 12.sp)

    val codeSpanAnnotator = DefaultAnnotatorSettings(
        linkTextSpanStyle = typography.textLink,
        codeSpanStyle = typography.inlineCode.toSpanStyle(),
        annotator = defaultAnnotator,
        referenceLinkHandler = referenceLinkHandler,
    )
    val makeSettings: (text: String) -> AnnotatorSettings = { text ->
        val style = typography.textLink.style!!.copy(color = Color.Transparent)
        val linkStyle = TextLinkStyles(
            style = style,
        )
        DefaultAnnotatorSettings(
            linkTextSpanStyle = if (spoilers[text] == true) typography.textLink else linkStyle,
            codeSpanStyle = typography.inlineCode.toSpanStyle(),
            annotator = RedditAnnotator(
                typography,
                referenceLinkHandler,
                spoilers,
                config,
                depth + 1,
                linkInteractionListener,
                defaultAnnotator
            ),
            referenceLinkHandler = referenceLinkHandler,
            linkInteractionListener = linkInteractionListener
                ?: if (spoilers[text] == true || depth > 0) {
                    null
                } else {
                    LinkInteractionListener { spoilers[text] = true }
                }
        )

    }
    override val annotate: (AnnotatedString.Builder.(content: String, child: ASTNode) -> Boolean) =
        { content, child ->
            val start = child.startOffset
            val end = child.endOffset.coerceAtMost(content.length)
            val text = content.substring(start until end)
            // Do not render superscript & spoilers in code span
            if (child.isInCode()) {
                buildMarkdownAnnotatedString(content, child.children, codeSpanAnnotator)
                false
            } else when (child.type) {

                MarkdownTokenTypes.TEXT -> {
                    val redditLinksPattern = Regex("(\\p{Punct}|\\s)?/?([ru]/\\w{2,24}/?)")
                    val text = child.getUnescapedTextInNode(content)
                    val links = redditLinksPattern.findAll(text)
                    var lastEnd = 0
                    for (l in links) {
                        append(text.substring(lastEnd, l.range.first))
                        val dest = l.groupValues[2]
                        val url = "https://www.reddit.com/$dest"
                        withStyle(typography.textLink.style!!) {
                            withLink(LinkAnnotation.Url(url)) {
                                append(l.value)
                            }
                        }
                        lastEnd = l.range.last + 1
                    }

                    if (lastEnd < text.length) {
                        append(text.substring(lastEnd))
                    }

                    true
                }

                RedditFlavourElementType.SUPERSCRIPT if child.children.size > 1 -> {
                    withStyle(superscriptStyle) {
                        buildMarkdownAnnotatedString(
                            content,
                            child.children.removeSuperscriptParenthesis(),
                            makeSettings(content)
                        )
                    }
                    true
                }

                RedditFlavourElementType.SUPERSCRIPT -> {
                    append("^")
                    true
                }

                RedditFlavourElementType.SPOILER if depth == 0 -> {
                    //appendInlineContent("SPOILER", child.getUnescapedTextInNode(content))
                    val settings = makeSettings(text)
                    pushStringAnnotation(tag = "SPOILER", annotation = text)
                    withStyle(
                        SpanStyle(
                            color = if (spoilers[text] == true) Color.White else Color.Transparent,
                            background = if (spoilers[text] == true) Color.Unspecified else Color.White
                        )
                    ) {
                        addLink(
                            LinkAnnotation.Clickable(
                                tag = "SPOILER",
                                linkInteractionListener = settings.linkInteractionListener
                            ),
                            0,
                            text.length
                        )
                        buildMarkdownAnnotatedString(
                            content,
                            child.children.removeSpoilerMarker(),
                            makeSettings(text)
                        )
                    }
                    pop()
                    true
                }

                RedditFlavourElementType.SPOILER -> {
                    buildMarkdownAnnotatedString(
                        content,
                        child.children.removeSpoilerMarker(),
                        makeSettings(text)
                    )
                    true
                }

                else -> {
                    false
                }
            }
        }
}

internal fun ASTNode.isInCode(): Boolean {
    return getParentOfType(MarkdownElementTypes.CODE_SPAN, MarkdownElementTypes.CODE_BLOCK) != null
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

internal fun List<ASTNode>.removeSuperscriptParenthesis(): List<ASTNode> {
    // First element is always SUPERSCRIPT
    if (size <= 1) return this
    val start = if (this.getOrNull(1)?.type == MarkdownTokenTypes.LPAREN) {
        2
    } else {
        1
    }
    val end = if (this.lastOrNull()?.type == MarkdownTokenTypes.RPAREN) {
        size - 1
    } else {
        size
    }

    return this.subList(start, end)
}