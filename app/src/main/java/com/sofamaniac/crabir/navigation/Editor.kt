package com.sofamaniac.crabir.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.navigation.routes.CrosspostCreatorRoute
import com.sofamaniac.crabir.navigation.routes.DraftsRoute
import com.sofamaniac.crabir.navigation.routes.MessageEditorRoute
import com.sofamaniac.crabir.navigation.routes.PostCreatorRoute
import com.sofamaniac.crabir.navigation.routes.TextEditorRoute
import com.sofamaniac.crabir.ui.drafts.DraftsView
import com.sofamaniac.crabir.ui.editor.TextEditor
import com.sofamaniac.crabir.ui.editor.crosspostEditor.CrosspostCreator
import com.sofamaniac.crabir.ui.editor.postEditor.PostCreator
import com.sofamaniac.crabir.ui.inbox.MessageEditor
import kotlin.reflect.typeOf

fun NavGraphBuilder.editorGraph(navController: NavController) {
    composable<PostCreatorRoute>(
        typeMap = mapOf(typeOf<Fullname?>() to NullableFullnameType)
    ) {
        val route = it.toRoute<PostCreatorRoute>()
        PostCreator(
            kind = route.kind,
            communitySlug = route.communityNamePrefixed,
            draftId = route.draftId,
            onDismissRequest = {
                navController.popBackStack()
            }
        )
    }

    composable<CrosspostCreatorRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<CrosspostCreatorRoute>()
        CrosspostCreator(route.post)
    }

    composable<MessageEditorRoute>(
        typeMap = mapOf(typeOf<Fullname?>() to NullableFullnameType)
    ) {
        val route = it.toRoute<MessageEditorRoute>()
        MessageEditor(parent = route.parent)
    }

    composable<TextEditorRoute>(
        typeMap = mapOf(typeOf<Fullname>() to FullnameType)
    ) {
        val route = it.toRoute<TextEditorRoute>()
        TextEditor(route.name, route.initial)
    }

    composable<DraftsRoute> {
        DraftsView()
    }
}
