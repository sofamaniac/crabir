package com.sofamaniac.redditmarkdown.redditFlavour

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache

class SuperscriptParser : SequentialParser {
    override fun parse(
        tokens: TokensCache,
        rangesToGlue: List<IntRange>
    ): SequentialParser.ParsingResult {
        val result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator = tokens.RangesListIterator(rangesToGlue)
        var lastOpenedPos: Int? = null
        var hasParenthesis = false

        while (iterator.type != null) {
            if (lastOpenedPos == null && iterator.type == RedditFlavourElementType.SUPERSCRIPT) {
                lastOpenedPos = iterator.index
                iterator = iterator.advance()
                hasParenthesis = iterator.type == MarkdownTokenTypes.LPAREN
                continue
            }

            if (lastOpenedPos != null) {
                val foundClosing: Boolean =
                    (hasParenthesis && iterator.type == MarkdownTokenTypes.RPAREN)
                            || (!hasParenthesis && (iterator.type == MarkdownTokenTypes.TEXT || iterator.type == MarkdownTokenTypes.WHITE_SPACE))

                if (foundClosing) {
                    iterator = iterator.advance()
                    result.withNode(
                        SequentialParser.Node(
                            lastOpenedPos..iterator.index,
                            RedditFlavourElementType.SUPERSCRIPT
                        )
                    )
                    lastOpenedPos = null
                    continue
                }
            }
            delegateIndices.put(iterator.index)
            iterator = iterator.advance()
        }
        if (lastOpenedPos != null && !hasParenthesis) {
            result.withNode(
                SequentialParser.Node(
                    lastOpenedPos..iterator.index,
                    RedditFlavourElementType.SUPERSCRIPT
                )
            )
        }
        return result.withFurtherProcessing(delegateIndices.get())
    }
}