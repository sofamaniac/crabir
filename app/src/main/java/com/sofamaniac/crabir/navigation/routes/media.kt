package com.sofamaniac.crabir.navigation.routes

import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.Serializable

@Serializable
data class SimpleImageRoute(val url: String) : Route

@Serializable
class FullscreenImageRoute(val post: Fullname) : Route

@Serializable
class FullscreenVideoRoute(val post: Fullname) : Route

@Serializable
class FullscreenGalleryRoute(val post: Fullname, val page: Int = 0) : Route
