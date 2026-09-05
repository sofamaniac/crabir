package com.sofamaniac.redditmarkdown.redditFlavour

import com.sofamaniac.redditmarkdown.redditFlavour.lexer._RFMLexer
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.StrikeThroughDelimiterParser
import org.intellij.markdown.lexer.MarkdownLexer
import org.intellij.markdown.parser.MarkerProcessorFactory
import org.intellij.markdown.parser.sequentialparsers.EmphasisLikeParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParserManager
import org.intellij.markdown.parser.sequentialparsers.impl.BacktickParser
import org.intellij.markdown.parser.sequentialparsers.impl.EmphStrongDelimiterParser
import org.intellij.markdown.parser.sequentialparsers.impl.ImageParser
import org.intellij.markdown.parser.sequentialparsers.impl.InlineLinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.ReferenceLinkParser

class RedditFlavourDescriptor(val enableImages: Boolean) : GFMFlavourDescriptor() {
    override fun createInlinesLexer(): MarkdownLexer {
        return MarkdownLexer(_RFMLexer())
    }

    override val markerProcessorFactory: MarkerProcessorFactory =
        RedditFlavourMarkerProcessor.Factory

    override val sequentialParserManager: SequentialParserManager =
        object : SequentialParserManager() {
            override fun getParserSequence(): List<SequentialParser> {
                return buildList {
                    add(SpoilerParser())
                    add(SuperscriptParser())
                    add(BacktickParser())
                    if (enableImages) add(ImageParser())
                    add(InlineLinkParser())
                    add(ReferenceLinkParser())
                    add(
                        EmphasisLikeParser(
                            EmphStrongDelimiterParser(),
                            StrikeThroughDelimiterParser()
                        )
                    )
                }
            }
        }

}
