package com.sofamaniac.redditmarkdown.redditFlavour

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache


class SpoilerParser : SequentialParser {
    val lastOpenedPos = mutableListOf<Int>()
    override fun parse(
        tokens: TokensCache,
        rangesToGlue: List<IntRange>
    ): SequentialParser.ParsingResult {
        val result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator = tokens.RangesListIterator(rangesToGlue)

        while (iterator.type != null) {
            if (iterator.type == RedditFlavourElementType.SPOILER_START) {
                lastOpenedPos.add(iterator.index)
                iterator = iterator.advance()
                continue
            }


            // Looking for opening
            if (iterator.type == MarkdownTokenTypes.GT && iterator.rawLookup(1) == MarkdownTokenTypes.EXCLAMATION_MARK) {
                lastOpenedPos.add(iterator.index)
                iterator = iterator.advance().advance()
                continue
            }

            // Looking for closing
            if (lastOpenedPos.isNotEmpty()
                && iterator.type == MarkdownTokenTypes.EXCLAMATION_MARK
                && iterator.rawLookup(1) == MarkdownTokenTypes.LT
            ) {
                iterator = iterator.advance()
                result.withNode(
                    SequentialParser.Node(
                        lastOpenedPos.removeAt(lastOpenedPos.lastIndex)..iterator.index + 1,
                        RedditFlavourElementType.SPOILER
                    )
                )
                iterator = iterator.advance()
                continue
            }
            delegateIndices.put(iterator.index)
            iterator = iterator.advance()
        }
        // Mark everything up to the end of the current as spoiler if missing closing marker
        // And make the beginning of the next line a spoiler
        if (lastOpenedPos.isNotEmpty()) {
            result.withNode(
                SequentialParser.Node(
                    lastOpenedPos.removeAt(lastOpenedPos.lastIndex)..iterator.index,
                    RedditFlavourElementType.SPOILER
                )
            )
            for (i in 0 until lastOpenedPos.size) {
                lastOpenedPos[i] = 0
            }
        }
        return result.withFurtherProcessing(delegateIndices.get())
    }
}