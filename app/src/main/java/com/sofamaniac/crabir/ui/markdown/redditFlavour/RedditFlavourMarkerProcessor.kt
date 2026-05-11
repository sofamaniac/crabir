package com.sofamaniac.crabir.ui.markdown.redditFlavour

import org.intellij.markdown.flavours.commonmark.CommonMarkMarkerProcessor
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider
import org.intellij.markdown.parser.MarkerProcessor
import org.intellij.markdown.parser.MarkerProcessorFactory
import org.intellij.markdown.parser.ProductionHolder
import org.intellij.markdown.parser.markerblocks.MarkerBlockProvider

class RedditFlavourMarkerProcessor(
    productionHolder: ProductionHolder,
    constraintsBase: RedditMarkdownConstraints,
) : CommonMarkMarkerProcessor(productionHolder, constraintsBase) {

    private val markerBlockProviders = super.getMarkerBlockProviders().plus(
        listOf(
            GitHubTableMarkerProvider(),
        )
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