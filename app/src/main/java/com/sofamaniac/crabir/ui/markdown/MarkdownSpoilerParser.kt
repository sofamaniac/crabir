package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.inlineparser.InlineProcessor
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import org.commonmark.internal.util.Parsing
import org.commonmark.node.Block
import org.commonmark.node.BlockQuote
import org.commonmark.node.CustomNode
import org.commonmark.node.HtmlBlock
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.parser.block.AbstractBlockParser
import org.commonmark.parser.block.AbstractBlockParserFactory
import org.commonmark.parser.block.BlockContinue
import org.commonmark.parser.block.BlockStart
import org.commonmark.parser.block.MatchedBlockParser
import org.commonmark.parser.block.ParserState
import java.util.regex.Pattern

const val SPOILER_OPEN = "\ue000"
const val SPOILER_CLOSE = "\ue000"
class SpoilerInline(val content: String) : CustomNode()

class SpoilerInlineProcessor : InlineProcessor() {

    private val pattern = Pattern.compile("$SPOILER_OPEN(.*?)$SPOILER_CLOSE", Pattern.DOTALL)

    override fun specialCharacter(): Char {
        return '\ue000'
    }

    override fun parse(): Node? {
        Log.d("SpoilerInlineProcessor", "parse: ${match(pattern)}")
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

    override fun configureParser(builder: Parser.Builder) {
        val blocks = CorePlugin.enabledBlockTypes()
        blocks.remove(HtmlBlock::class.java)
        blocks.remove(BlockQuote::class.java)
        builder.customBlockParserFactory(BlockQuoteParser.Factory())
        builder.enabledBlockTypes(blocks)
    }

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(MarkwonInlineParserPlugin::class.java)
            .factoryBuilder().addInlineProcessor(SpoilerInlineProcessor())
    }

    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
        builder.on(SpoilerInline::class.java) { visitor, spoilerInline ->
            val length = visitor.length()
            visitor.builder().append(spoilerInline.content)
            visitor.setSpans(length, SpoilerSpan())
        }
    }

    companion object {
        fun create(): RedditSpoilerPlugin {
            return RedditSpoilerPlugin()
        }
    }
}

class BlockQuoteParser : AbstractBlockParser() {
    private val block = BlockQuote()

    companion object {
        fun startBlock(state: ParserState, index: Int): Boolean {
            val line = state.line
            return state.indent < Parsing.CODE_BLOCK_INDENT && line.getOrNull(index) == '>'
        }

        fun startSpoiler(state: ParserState, index: Int): Boolean {
            val line = state.line
            return startBlock(state, index) && line.getOrNull(index + 1) == '!'
        }
    }

    override fun getBlock(): Block {
        return block
    }

    override fun canContain(childBlock: Block?): Boolean {
        return true
    }

    override fun isContainer(): Boolean {
        return true
    }

    override fun tryContinue(parserState: ParserState?): BlockContinue? {
        val nextNonWhitespace = parserState?.nextNonSpaceIndex
        if (startBlock(parserState!!, nextNonWhitespace!!)) {
            var start = parserState.column + parserState.indent + 1
            if (Parsing.isSpaceOrTab(parserState.line, nextNonWhitespace + 1)) {
                start += 1
            }
            return BlockContinue.atColumn(start)
        } else {
            return BlockContinue.none()
        }
    }

    class Factory : AbstractBlockParserFactory() {

        override fun tryStart(
            state: ParserState?,
            matchedBlockParser: MatchedBlockParser?
        ): BlockStart? {
            val nextNonWhitespace = state?.nextNonSpaceIndex
            if (nextNonWhitespace != null) {
                if (startSpoiler(state, nextNonWhitespace)) {
                    Log.d("BlockQuoteParser", "startSpoiler: ${state.line}")
                    return BlockStart.none()
                } else if (startBlock(state, nextNonWhitespace)) {
                    Log.d("BlockQuoteParser", "startBlock: ${state.line}")
                    var start = state.column + state.indent + 1
                    if (Parsing.isSpaceOrTab(state.line, nextNonWhitespace + 1)) {
                        start += 1
                    }
                    return BlockStart.of(BlockQuoteParser())
                        .atColumn(start)
                }
            }
            return BlockStart.none()
        }
    }

}

// Extension function for easier usage
fun Markwon.Builder.useRedditSpoilers(): Markwon.Builder {
    //return this.usePlugin(RedditSpoilerSpanFactoryPlugin())
    return this.usePlugin(RedditSpoilerPlugin.create())
}