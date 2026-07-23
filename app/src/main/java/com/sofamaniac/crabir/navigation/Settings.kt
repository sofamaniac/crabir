package com.sofamaniac.crabir.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.settings.DebugOptionsView
import com.sofamaniac.crabir.settings.GeneralSettingsPage
import com.sofamaniac.crabir.settings.GeneralSettingsRoute
import com.sofamaniac.crabir.settings.SettingsPage
import com.sofamaniac.crabir.settings.data.DataSettingsPage
import com.sofamaniac.crabir.settings.data.DataSettingsRoute
import com.sofamaniac.crabir.settings.filters.FiltersSettingsPage
import com.sofamaniac.crabir.settings.post.PostSettingsPage
import com.sofamaniac.crabir.settings.post.PostSettingsRoute
import com.sofamaniac.crabir.settings.theme.ThemeEditor
import com.sofamaniac.crabir.settings.theme.ThemeSettingsPage
import com.sofamaniac.crabir.settings.views.ViewsSettingsPage

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    composable<LicensesRoute> {
        val libraries by produceLibraries(R.raw.aboutlibraries)
        LibrariesContainer(libraries, modifier = Modifier.fillMaxSize())
    }
    composable<SettingsRoute> {
        SettingsPage()
    }
    composable<GeneralSettingsRoute> {
        GeneralSettingsPage()
    }
    composable<ThemeRoute> {
        ThemeSettingsPage()
    }
    composable<ThemeEditorRoute> {
        ThemeEditor()
    }
    composable<ViewsSettingRoute> {
        ViewsSettingsPage()
    }
    composable<FiltersSettingRoute> {
        FiltersSettingsPage()
    }
    composable<PostSettingsRoute> {
        PostSettingsPage()
    }
    composable<DebugOptionsRoute> {
        DebugOptionsView()
    }
    composable<DataSettingsRoute> {
        DataSettingsPage()
    }
}