package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.editor.CrosspostCreator
import com.sofamaniac.crabir.ui.editor.postEditor.PostCreator
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
class PostCreatorRoute(val kind: Kind, val communitySlug: String?) : Route

@Serializable
class CrosspostCreatorRoute(val post: Fullname) : Route

fun NavGraphBuilder.editorGraph(navController: NavController) {
    composable<PostCreatorRoute>(
        typeMap = mapOf(typeOf<Fullname?>() to NullableFullnameType)
    ) {
        val route = it.toRoute<PostCreatorRoute>()
        PostCreator(kind = route.kind, communitySlug = route.communitySlug, onDismissRequest = {
            navController.popBackStack()
        })
    }

    composable<CrosspostCreatorRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<CrosspostCreatorRoute>()
        CrosspostCreator(route.post)
    }
}
