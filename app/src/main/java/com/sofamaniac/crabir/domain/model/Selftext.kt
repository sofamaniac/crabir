/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Selftext(
    val markdown: ParsedMarkdown,
    val html: String,
    val richtext: RichtextDocument = RichtextDocument(emptyList()),
) {

    companion object {
        val DUMMY = Selftext(ParsedMarkdown("selftext"), "selftextHtml")
    }
}
