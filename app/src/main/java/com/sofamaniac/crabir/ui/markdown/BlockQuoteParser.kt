package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import org.commonmark.internal.util.Parsing
import org.commonmark.node.Block
import org.commonmark.node.BlockQuote
import org.commonmark.parser.block.AbstractBlockParser
import org.commonmark.parser.block.AbstractBlockParserFactory
import org.commonmark.parser.block.BlockContinue
import org.commonmark.parser.block.BlockStart
import org.commonmark.parser.block.MatchedBlockParser
import org.commonmark.parser.block.ParserState

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
        if (startBlock(parserState!!, nextNonWhitespace!!)
            && !startSpoiler(parserState, nextNonWhitespace)
        ) {
            Log.d("BlockQuoteParser", "found continuation ${parserState.line}")
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
            Log.d("BlockQuoteParser", "try start: '${state?.line}'")
            Log.d(
                "BlockQuoteParser",
                "try start: ${state?.line?.getOrNull(nextNonWhitespace ?: 0)},  ${
                    state?.line?.getOrNull(
                        (nextNonWhitespace ?: 0) + 1
                    )
                }"
            )
            when {
                nextNonWhitespace == null -> {
                    return BlockStart.none()
                }

                startSpoiler(state, nextNonWhitespace) -> {
                    Log.d("BlockQuoteParser", "startSpoiler: ${state.line}")
                    return BlockStart.none()
                }

                startBlock(state, nextNonWhitespace) -> {
                    Log.d("BlockQuoteParser", "startBlock: ${state.line}")
                    var start = state.column + state.indent + 1
                    if (Parsing.isSpaceOrTab(state.line, nextNonWhitespace + 1)) {
                        start += 1
                    }
                    return BlockStart.of(BlockQuoteParser())
                        .atColumn(start)
                }

                else -> return BlockStart.none()
            }
        }
    }

}