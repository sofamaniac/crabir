/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:14 PM
 *
 */

package com.sofamaniac.crabir

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.sofamaniac.crabir.settings.SettingsPage
import com.sofamaniac.crabir.settings.theme.ConfigureMaterialTheme
import com.sofamaniac.crabir.settings.theme.CrabirTheme
import com.sofamaniac.crabir.settings.theme.DefaultDarkTheme
import com.sofamaniac.crabir.settings.theme.ThemeEditor
import com.sofamaniac.crabir.settings.theme.ThemeSettingsPage
import com.sofamaniac.crabir.settings.theme.rememberAppTheme
import com.sofamaniac.crabir.settings.views.ViewsSettingsPage
import com.sofamaniac.crabir.ui.InboxView
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayerManager
import com.sofamaniac.crabir.ui.search.SearchTab
import com.sofamaniac.crabir.ui.subreddit.HistoryViewer
import com.sofamaniac.crabir.ui.subreddit.HomeViewer
import com.sofamaniac.crabir.ui.subreddit.MultiView
import com.sofamaniac.crabir.ui.subreddit.SubredditViewer
import com.sofamaniac.crabir.ui.subredditList.SubredditListViewer
import com.sofamaniac.crabir.ui.thread.ThreadView
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.user.ProfileView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch


@HiltAndroidApp
class CrabirApp : Application()

val LocalTheme = compositionLocalOf<CrabirTheme> { DefaultDarkTheme }
val LocalDrawerState = compositionLocalOf<DrawerState> { error("No drawer state provided") }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConfigureMaterialTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                // Setup nav controller
                CompositionLocalProvider(LocalNavController provides navController) {
                    val theme = rememberAppTheme()
                    Log.d("MainActivity", "onCreate: $theme")
                    CompositionLocalProvider(LocalTheme provides theme) {
                        CompositionLocalProvider(LocalDrawerState provides drawerState) {
                            MainScreen(
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    DisposableEffect(Unit) {
        onDispose {
            VideoPlayerManager.releasePlayer()
        }
    }

    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val drawerState = LocalDrawerState.current
    BackHandler {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        } else {
            // TODO: ask for confirmation and exit the app
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            } else {
                activity?.finish()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = LocalDrawerState.current,
        drawerContent = {
            DrawerContent()
        },
    ) {
        NavigationGraph(
            navController,
        )
    }


}

val BASE_URL = listOf(
    "reddit.com",
    "www.reddit.com",
    "old.reddit.com",
    "new.reddit.com",
)


inline fun <reified T : Any> makeDeepLinks(url: String): List<NavDeepLink> {
    val links = BASE_URL.map {
        navDeepLink<T>(basePath = "$it/$url")
    }
    Log.d("makeDeepLinks", "Generating links for $url")
    for (link in links) {
        Log.d("makeDeepLinks", link.uriPattern.toString())
    }
    return links
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier.fillMaxSize(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<HomeRoute> {
            HomeViewer()
        }
        composable(
            route = PostRoute.ROUTE,
            deepLinks = makeDeepLinks<String>(url = "r/{subreddit}/comments/{id}/{title}"),
            arguments = listOf(
                navArgument("subreddit") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
            )
        )
        {
            val subreddit = it.arguments?.getString("subreddit")
            val id = it.arguments?.getString("id")
            val title = it.arguments?.getString("title")
            val permalink = if (subreddit != null && id != null && title != null) {
                "/r/$subreddit/comments/$id/$title"
            } else {
                null
            }
            FullscreenHandler {
                ThreadView(permalink = permalink, dismiss = { navController.popBackStack() })
            }
        }
        composable<SubscriptionsRoute> {
            SubredditListViewer(navController = navController)
        }
        composable<SearchRoute> { navBackStackEntry ->
            val search = navBackStackEntry.toRoute<SearchRoute>()
            SearchTab(search)
        }
        composable<InboxRoute> {
            InboxView()

        }
        composable<SubredditRoute>(
            deepLinks = makeDeepLinks<SubredditRoute>(url = "r")
        )
        { navBackStackEntry ->
            val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
            SubredditViewer(
                subreddit,
            )
        }
        composable<MultiRoute> { navBackStackEntry ->
            val permalink = navBackStackEntry.toRoute<MultiRoute>().permalink
            val name = navBackStackEntry.toRoute<MultiRoute>().displayName
            MultiView(
                name,
                permalink,
            )
        }
        composable(
            route = "user/{author}",
            deepLinks = makeDeepLinks<ProfileRoute>(url = "user/{author}"),
            arguments = listOf(
                navArgument("author") { type = NavType.StringType }
            )
        ) {
            val params = it.toRoute<ProfileRoute>()
            ProfileView(
                params.author,
            )
        }
        composable<ProfileRoute>(
            deepLinks = makeDeepLinks<ProfileRoute>(url = "user")
                    + makeDeepLinks<ProfileRoute>(url = "u")
        ) { navBackStackEntry ->
            val params = navBackStackEntry.toRoute<ProfileRoute>()
            val tab = ProfileTabs.fromString(params.tab)
            ProfileView(
                params.author,
                initialTab = tab
            )
        }
        composable(
            route = "user/{author}",
            arguments = listOf(
                navArgument("author") { type = NavType.StringType }
            ),
            deepLinks = makeDeepLinks<String>(url = "user/{author}")
        ) { navBackStackEntry ->
            val author = navBackStackEntry.arguments?.getString("author")!!
            ProfileView(
                author,
            )
        }
        composable<LicensesRoute> {
            val libraries by produceLibraries(R.raw.aboutlibraries)
            LibrariesContainer(libraries, modifier = Modifier.fillMaxSize())
        }
        composable<SettingsRoute> {
            SettingsPage()
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
        composable<HistoryRoute> {
            HistoryViewer()
        }
    }
}