/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Selftext(
    private val selftext: String,
    private val selftextHtml: String
) {
    val markdown: String get() = selftext
    val html: String get() = selftextHtml

    companion object {
        val DUMMY = Selftext("selftext", "selftextHtml")
    }
}