package com.sofamaniac.crabir.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.sofamaniac.crabir.LocalApiSettings
import com.sofamaniac.crabir.settings.api.ApiSettingsRoute
import com.sofamaniac.crabir.ui.inbox.InboxView
import com.sofamaniac.crabir.ui.postFeed.history.HistoryViewer
import com.sofamaniac.crabir.ui.postFeed.home.HomeViewer
import com.sofamaniac.crabir.ui.search.SearchTab
import com.sofamaniac.crabir.ui.subredditList.SubredditListViewer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val startRoute = if (!LocalApiSettings.current.isConfigured) {
        ApiSettingsRoute
    } else {
        HomeRoute
    }
    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        //        enterTransition = { EnterTransition.None },
        //        exitTransition = { ExitTransition.None },
    ) {
        composable<HomeRoute> {
            HomeViewer()
        }

        profileGraph()
        postGraph(navController = navController)
        subredditGraph(navController = navController)
        imagesGraph(navController = navController)
        settingsGraph()
        editorGraph(navController = navController)

        composable<SubscriptionsRoute> {
            SubredditListViewer()
        }
        composable<SearchRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = SearchRoute.URL
                }
            )
        ) { navBackStackEntry ->
            val search = navBackStackEntry.toRoute<SearchRoute>()
            SearchTab(search)
        }
        composable<InboxRoute> {
            InboxView()

        }
        composable<HistoryRoute> {
            HistoryViewer()
        }
    }
}
