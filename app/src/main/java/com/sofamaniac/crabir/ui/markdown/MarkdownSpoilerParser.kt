package com.sofamaniac.crabir.ui.markdown

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.TextView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.inlineparser.InlineParserUtils.mergeChildTextNodes
import io.noties.markwon.inlineparser.InlineProcessor
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import org.commonmark.internal.Delimiter
import org.commonmark.node.BlockQuote
import org.commonmark.node.CustomNode
import org.commonmark.node.HtmlBlock
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import java.util.EmptyStackException
import java.util.Stack
import java.util.regex.Pattern

class SpoilerInline(val content: String = "", val depth: Int = 0) : CustomNode()

class SpoilerOpening(
    /** Node that contains the opening marker */
    val node: SpoilerInline,

    /**
     * Previous delimiter (emphasis, etc.) before this bracket.
     */
    val previousDelimiter: Delimiter?,
)

class SpoilerStack {
    private var stack: Stack<SpoilerOpening> = Stack()

    // Spoilers cannot span across blocks so we keep track of the current block
    private var currentBlock: Node? = null

    fun clear() {
        stack.clear()
    }

    fun pop(block: Node): SpoilerOpening? {
        updateBlock(block)
        return try {
            stack.pop()
        } catch (_: EmptyStackException) {
            null
        }
    }

    fun add(block: Node, lastDelimiter: Delimiter?): Node {
        updateBlock(block)
        val node = SpoilerInline(depth = stack.size)
        stack.push(SpoilerOpening(node, lastDelimiter))
        return node
    }

    private fun updateBlock(block: Node) {
        if (block != currentBlock) {
            clear()
        }
        currentBlock = block
    }
}

class SpoilerOpeningInline(val stack: SpoilerStack) : InlineProcessor() {
    override fun specialCharacter(): Char {
        return '>'
    }

    override fun parse(): Node? {
        index++
        if (peek() == '!') {
            index++
            return stack.add(block, lastDelimiter())
        }
        return null
    }
}

class SpoilerClosingInline(val stack: SpoilerStack) : InlineProcessor() {
    override fun specialCharacter(): Char {
        return '!'
    }

    override fun parse(): Node? {
        index++
        if (peek() != '<') {
            return null
        }
        index++
        val marker = stack.pop(block) ?: return null
        val node = marker.node
        var child = node.next
        while (child != null) {
            node.appendChild(child)
            child = node.next
        }
        processDelimiters(marker.previousDelimiter)
        mergeChildTextNodes(node)
        //marker.node?.unlink()
        return node
    }
}

class SpoilerInlineProcessor : InlineProcessor() {

    private val pattern = Pattern.compile(">!(.*?)!<", Pattern.DOTALL)

    override fun specialCharacter(): Char {
        return '>'
    }

    override fun parse(): Node? {
        val spoiler = match(pattern)
        if (spoiler == null) {
            return null
        } else {
            val content = spoiler.substring(2, spoiler.length - 2)
            val spoilerNode = SpoilerInline(content)
            return spoilerNode
        }
    }

}

/**
 * Markwon plugin for Reddit spoilers
 */
class RedditSpoilerPlugin : AbstractMarkwonPlugin() {

    val stack = SpoilerStack()

    override fun configureParser(builder: Parser.Builder) {
        val blocks = CorePlugin.enabledBlockTypes()
        blocks.remove(HtmlBlock::class.java)
        blocks.remove(BlockQuote::class.java)
        builder.customBlockParserFactory(BlockQuoteParser.Factory())
        builder.enabledBlockTypes(blocks)
    }

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(MarkwonInlineParserPlugin::class.java)
            .factoryBuilder()
            .addInlineProcessor(SpoilerOpeningInline(stack))
            .addInlineProcessor(SpoilerClosingInline(stack))
    }

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(
            SpoilerInline::class.java
        ) { p0, p1 -> SpoilerSpan() }
    }

    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
        builder.on(SpoilerInline::class.java) { visitor, spoilerInline ->
            val length = visitor.length()
            visitor.visitChildren(spoilerInline)
            if (spoilerInline.depth == 0) {
                visitor.setSpansForNode(spoilerInline, length)
            }
        }
    }

    override fun afterRender(node: Node, visitor: MarkwonVisitor) {
        super.afterRender(node, visitor)
        stack.clear()
    }

    override fun afterSetText(textView: TextView) {
        val text = textView.text
        if (text is Spanned) {
            val spoilerSpans = text.getSpans(0, text.length, SpoilerSpan::class.java)
            val builder = SpannableStringBuilder(text)
            for (span in spoilerSpans) {
                val start = text.getSpanStart(span)
                val end = text.getSpanEnd(span)
                val flags = text.getSpanFlags(span)
                builder.removeSpan(span)
                builder.setSpan(span, start, end, flags)
            }
            textView.text = builder
        }
    }

    companion object {
        fun create(): RedditSpoilerPlugin {
            return RedditSpoilerPlugin()
        }
    }
}

// Extension function for easier usage
fun Markwon.Builder.useRedditSpoilers(): Markwon.Builder {
    //return this.usePlugin(RedditSpoilerSpanFactoryPlugin())
    return this.usePlugin(RedditSpoilerPlugin.create())
}