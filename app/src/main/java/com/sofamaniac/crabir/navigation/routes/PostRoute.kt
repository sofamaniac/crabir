package com.sofamaniac.crabir.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
class PostRoute(val postPermalink: String, val comment: String? = null, val context: Int? = null) :
    Route
