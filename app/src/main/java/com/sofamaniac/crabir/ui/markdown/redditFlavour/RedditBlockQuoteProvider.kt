package com.sofamaniac.crabir.ui.markdown.redditFlavour

import android.util.Log
import org.intellij.markdown.parser.LookaheadText
import org.intellij.markdown.parser.MarkerProcessor
import org.intellij.markdown.parser.ProductionHolder
import org.intellij.markdown.parser.constraints.MarkdownConstraints
import org.intellij.markdown.parser.constraints.getCharsEaten
import org.intellij.markdown.parser.markerblocks.MarkerBlock
import org.intellij.markdown.parser.markerblocks.MarkerBlockProvider
import org.intellij.markdown.parser.markerblocks.impl.BlockQuoteMarkerBlock

class RedditBlockQuoteProvider : MarkerBlockProvider<MarkerProcessor.StateInfo> {
    override fun createMarkerBlocks(
        pos: LookaheadText.Position,
        productionHolder: ProductionHolder,
        stateInfo: MarkerProcessor.StateInfo
    ): List<MarkerBlock> {
        val currentConstraints = stateInfo.currentConstraints
        val nextConstraints = stateInfo.nextConstraints
        return if (pos.offsetInCurrentLine != currentConstraints.getCharsEaten(pos.currentLine)) {
            emptyList()
        } else if (nextConstraints != currentConstraints && nextConstraints.types.lastOrNull() == '>') {
            if (isSpoilerStart(pos)) {
                Log.d("BlockQuoteProvider", "Emitting SPOILER")
                listOf(SpoilerMarkerBlock(currentConstraints, productionHolder.mark()))
            } else if (matches(pos)) {
                listOf(
                    BlockQuoteMarkerBlock(
                        nextConstraints,
                        productionHolder.mark()
                    )
                )
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    private fun matches(pos: LookaheadText.Position): Boolean {
        if (pos.offsetInCurrentLine != -1) return false
        val text = pos.currentLineFromPosition
        val offset = MarkerBlockProvider.passSmallIndent(text)
        return text.getOrNull(offset) == '>'
    }

    private fun isSpoilerStart(pos: LookaheadText.Position): Boolean {
        val text = pos.currentLineFromPosition
        val offset = MarkerBlockProvider.passSmallIndent(text)
        return text.getOrNull(offset) == '>' && text.getOrNull(offset + 1) == '!'
    }

    override fun interruptsParagraph(
        pos: LookaheadText.Position,
        constraints: MarkdownConstraints
    ): Boolean {
        return false
    }
}