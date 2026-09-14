package com.sofamaniac.crabir.navigation.routes

import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import kotlinx.serialization.Serializable

@Serializable
class PostCreatorRoute(
    val kind: Kind,
    val communityNamePrefixed: String?,
    val draftId: String? = null,
) :
    Route

@Serializable
class CrosspostCreatorRoute(val post: Fullname) : Route

@Serializable
class MessageEditorRoute(val parent: Fullname? = null) : Route

@Serializable
class TextEditorRoute(val name: Fullname, val initial: String) : Route

@Serializable
object DraftsRoute : Route
