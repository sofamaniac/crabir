package com.sofamaniac.redditmarkdown.redditFlavour

import org.intellij.markdown.IElementType
import org.intellij.markdown.parser.LookaheadText
import org.intellij.markdown.parser.ProductionHolder
import org.intellij.markdown.parser.constraints.MarkdownConstraints
import org.intellij.markdown.parser.markerblocks.MarkerBlock
import org.intellij.markdown.parser.markerblocks.MarkerBlockImpl

class SpoilerMarkerBlock(myConstraints: MarkdownConstraints, marker: ProductionHolder.Marker) :
    MarkerBlockImpl(myConstraints, marker) {


    override fun calcNextInterestingOffset(pos: LookaheadText.Position): Int {
        return pos.offset
    }


    override fun doProcessToken(
        pos: LookaheadText.Position,
        currentConstraints: MarkdownConstraints
    ): MarkerBlock.ProcessingResult {

        return MarkerBlock.ProcessingResult.DEFAULT
    }

    override fun getDefaultAction(): MarkerBlock.ClosingAction {
        return MarkerBlock.ClosingAction.NOTHING
    }

    override fun allowsSubBlocks(): Boolean = false

    override fun isInterestingOffset(pos: LookaheadText.Position): Boolean {
        return false
    }

    override fun getDefaultNodeType(): IElementType {
        return RedditFlavourElementType.SPOILER_START
    }
}