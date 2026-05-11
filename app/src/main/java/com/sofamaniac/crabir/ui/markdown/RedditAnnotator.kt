package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.annotator.AnnotatorSettings
import com.mikepenz.markdown.annotator.DefaultAnnotatorSettings
import com.mikepenz.markdown.annotator.buildMarkdownAnnotatedString
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownAnnotatorConfig
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.ReferenceLinkHandler
import com.sofamaniac.crabir.ui.markdown.redditFlavour.RedditFlavourElementType
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode

class RedditAnnotator(
    typography: MarkdownTypography,
    referenceLinkHandler: ReferenceLinkHandler,
    val spoilers: MutableMap<String, Boolean>,
    override var config: MarkdownAnnotatorConfig,
    val depth: Int = 0,
) : MarkdownAnnotator {

    val superscriptStyle =
        SpanStyle(baselineShift = BaselineShift.Companion.Superscript, fontSize = 12.sp)
    val makeSettings: (text: String) -> AnnotatorSettings = { text ->
        DefaultAnnotatorSettings(
            linkTextSpanStyle = typography.textLink,
            codeSpanStyle = typography.inlineCode.toSpanStyle(),
            annotator = RedditAnnotator(
                typography,
                referenceLinkHandler,
                spoilers,
                config,
                depth + 1
            ),
            referenceLinkHandler = referenceLinkHandler,
            linkInteractionListener = if (spoilers[text] == true || depth > 0) {
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
            if (child.type == RedditFlavourElementType.SUPERSCRIPT) {
                if (child.children.size > 1) {
                    withStyle(superscriptStyle) {
                        buildMarkdownAnnotatedString(
                            content,
                            child.children.removeSuperscriptParenthesis(),
                            makeSettings(content)
                        )
                    }
                } else {
                    append("^")
                }
            }
            when (child.type) {
                RedditFlavourElementType.SPOILER if depth == 0 -> {
                    //appendInlineContent("SPOILER", child.getUnescapedTextInNode(content))
                    pushStringAnnotation(tag = "SPOILER", annotation = text)
                    withStyle(
                        SpanStyle(
                            color = if (spoilers[text] == true) Color.Companion.White else Color.Companion.Transparent,
                            background = if (spoilers[text] == true) Color.Companion.Unspecified else Color.Companion.White
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

internal fun List<ASTNode>.removeSpoilerMarker(): List<ASTNode> {

    val start =
        if (this.firstOrNull()?.type == RedditFlavourElementType.SPOILER_START) {
            1
        } else if (this.getOrNull(0)?.type == MarkdownTokenTypes.Companion.GT
            && this.getOrNull(1)?.type == MarkdownTokenTypes.Companion.EXCLAMATION_MARK
        ) {
            2
        } else {
            0
        }
    val end =
        if (this.getOrNull(size - 1)?.type == MarkdownTokenTypes.Companion.LT
            && this.getOrNull(size - 2)?.type == MarkdownTokenTypes.Companion.EXCLAMATION_MARK
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
    val start = if (this.getOrNull(1)?.type == MarkdownTokenTypes.Companion.LPAREN) {
        2
    } else {
        1
    }
    val end = if (this.lastOrNull()?.type == MarkdownTokenTypes.Companion.RPAREN) {
        size - 1
    } else {
        size
    }

    return this.subList(start, end)
}