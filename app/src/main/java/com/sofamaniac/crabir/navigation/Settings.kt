package com.sofamaniac.crabir.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.sofamaniac.crabir.navigation.routes.SettingsPage
import com.sofamaniac.crabir.navigation.routes.SettingsRoute
import com.sofamaniac.crabir.navigation.routes.SortManagerRoute
import com.sofamaniac.crabir.navigation.routes.ViewManagerRoute
import com.sofamaniac.crabir.settings.SettingsPageAdaptive
import com.sofamaniac.crabir.settings.feedSettings.SortManagerPage
import com.sofamaniac.crabir.settings.views.ViewManagerPage
import kotlin.reflect.typeOf


fun NavGraphBuilder.settingsGraph() {
    composable<SettingsPage>(
        typeMap = mapOf(typeOf<SettingsRoute?>() to NullableSettingsRoute)
    ) {
        val route = it.toRoute<SettingsPage>()
        SettingsPageAdaptive(route.route)
    }
    dialog<ViewManagerRoute> {
        val navController = LocalNavController.current
        ViewManagerPage { navController?.navigateUp() }
    }

    dialog<SortManagerRoute> {
        val navController = LocalNavController.current
        SortManagerPage { navController?.navigateUp() }
    }
}
