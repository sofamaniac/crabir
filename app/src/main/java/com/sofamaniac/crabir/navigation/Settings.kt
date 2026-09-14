package com.sofamaniac.crabir.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sofamaniac.crabir.navigation.routes.ApiSettingsRoute
import com.sofamaniac.crabir.navigation.routes.CommentsSettingsRoute
import com.sofamaniac.crabir.navigation.routes.DataSettingsRoute
import com.sofamaniac.crabir.navigation.routes.DebugOptionsRoute
import com.sofamaniac.crabir.navigation.routes.FeedSettingsRoute
import com.sofamaniac.crabir.navigation.routes.FiltersSettingRoute
import com.sofamaniac.crabir.navigation.routes.GeneralSettingsRoute
import com.sofamaniac.crabir.navigation.routes.HistorySettingsRoute
import com.sofamaniac.crabir.navigation.routes.LateralMenuSettingsRoute
import com.sofamaniac.crabir.navigation.routes.LicensesRoute
import com.sofamaniac.crabir.navigation.routes.PostSettingsRoute
import com.sofamaniac.crabir.navigation.routes.SettingsRoute
import com.sofamaniac.crabir.navigation.routes.SortManagerRoute
import com.sofamaniac.crabir.navigation.routes.ThemeEditorRoute
import com.sofamaniac.crabir.navigation.routes.ThemeRoute
import com.sofamaniac.crabir.navigation.routes.ViewManagerRoute
import com.sofamaniac.crabir.navigation.routes.ViewsSettingRoute
import com.sofamaniac.crabir.settings.DebugOptionsView
import com.sofamaniac.crabir.settings.GeneralSettingsPage
import com.sofamaniac.crabir.settings.LicensePage
import com.sofamaniac.crabir.settings.SettingsPage
import com.sofamaniac.crabir.settings.api.ApiSettingsPage
import com.sofamaniac.crabir.settings.comments.CommentsSettingsPage
import com.sofamaniac.crabir.settings.data.DataSettingsPage
import com.sofamaniac.crabir.settings.feedSettings.FeedSettingsPage
import com.sofamaniac.crabir.settings.feedSettings.SortManagerPage
import com.sofamaniac.crabir.settings.filters.FiltersSettingsPage
import com.sofamaniac.crabir.settings.history.HistorySettingsPage
import com.sofamaniac.crabir.settings.lateralMenu.LateralMenuSettingsPage
import com.sofamaniac.crabir.settings.post.PostSettingsPage
import com.sofamaniac.crabir.settings.theme.ThemeEditor
import com.sofamaniac.crabir.settings.theme.ThemeSettingsPage
import com.sofamaniac.crabir.settings.views.ViewManagerPage
import com.sofamaniac.crabir.settings.views.ViewsSettingsPage

fun NavGraphBuilder.settingsGraph() {
    composable<LicensesRoute> {
        LicensePage()
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
    composable<ViewManagerRoute> {
        ViewManagerPage()
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
    composable<CommentsSettingsRoute> {
        CommentsSettingsPage()
    }
    composable<LateralMenuSettingsRoute> {
        LateralMenuSettingsPage()
    }
    composable<FeedSettingsRoute> {
        FeedSettingsPage()
    }
    composable<SortManagerRoute> {
        SortManagerPage()
    }
    composable<ApiSettingsRoute> {
        ApiSettingsPage()
    }
    composable<HistorySettingsRoute> {
        HistorySettingsPage()
    }
}
