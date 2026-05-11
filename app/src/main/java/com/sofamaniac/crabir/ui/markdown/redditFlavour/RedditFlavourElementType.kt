package com.sofamaniac.crabir.ui.markdown.redditFlavour

import org.intellij.markdown.MarkdownElementType

object RedditFlavourElementType {
    @JvmField
    val SPOILER_START = MarkdownElementType("SPOILER_START", isToken = true)

    @JvmField
    val SPOILER = MarkdownElementType("SPOILER")

    @JvmField
    val SUPERSCRIPT = MarkdownElementType("SUPERSCRIPT", isToken = true)
}