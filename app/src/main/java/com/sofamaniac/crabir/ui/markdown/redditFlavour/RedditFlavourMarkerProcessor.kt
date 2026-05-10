package com.sofamaniac.crabir.ui.markdown.redditFlavour

import org.intellij.markdown.flavours.commonmark.CommonMarkMarkerProcessor
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider
import org.intellij.markdown.parser.MarkerProcessor
import org.intellij.markdown.parser.MarkerProcessorFactory
import org.intellij.markdown.parser.ProductionHolder
import org.intellij.markdown.parser.markerblocks.MarkerBlockProvider
import org.intellij.markdown.parser.markerblocks.providers.AtxHeaderProvider
import org.intellij.markdown.parser.markerblocks.providers.BlockQuoteProvider
import org.intellij.markdown.parser.markerblocks.providers.CodeBlockProvider
import org.intellij.markdown.parser.markerblocks.providers.CodeFenceProvider
import org.intellij.markdown.parser.markerblocks.providers.HorizontalRuleProvider
import org.intellij.markdown.parser.markerblocks.providers.HtmlBlockProvider
import org.intellij.markdown.parser.markerblocks.providers.LinkReferenceDefinitionProvider
import org.intellij.markdown.parser.markerblocks.providers.ListMarkerProvider
import org.intellij.markdown.parser.markerblocks.providers.SetextHeaderProvider

class RedditFlavourMarkerProcessor(
    productionHolder: ProductionHolder,
    constraintsBase: RedditMarkdownConstraints,
) : CommonMarkMarkerProcessor(productionHolder, constraintsBase) {

    private val markerBlockProviders = listOf(
        CodeBlockProvider(),
        HorizontalRuleProvider(),
        CodeFenceProvider(),
        SetextHeaderProvider(),
        // Custom block quote provider to handle reddit spoilers
        //RedditBlockQuoteProvider(),
        BlockQuoteProvider(),
        ListMarkerProvider(),
        AtxHeaderProvider(),
        HtmlBlockProvider(),
        LinkReferenceDefinitionProvider(),
        GitHubTableMarkerProvider(),
    )

    override fun getMarkerBlockProviders(): List<MarkerBlockProvider<StateInfo>> {
        return markerBlockProviders
    }

    object Factory : MarkerProcessorFactory {
        override fun createMarkerProcessor(productionHolder: ProductionHolder): MarkerProcessor<*> {
            return RedditFlavourMarkerProcessor(productionHolder, RedditMarkdownConstraints.BASE)
        }
    }
}