/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:14 PM
 *
 */

package com.sofamaniac.reboost

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.sofamaniac.reboost.settings.DefaultReboostTheme
import com.sofamaniac.reboost.settings.ProvideReboostTheme
import com.sofamaniac.reboost.settings.ReboostTheme
import com.sofamaniac.reboost.settings.rememberAppTheme
import com.sofamaniac.reboost.ui.drawer.DrawerContent
import com.sofamaniac.reboost.ui.drawer.DrawerViewModel
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayerManager
import com.sofamaniac.reboost.ui.search.SearchTab
import com.sofamaniac.reboost.ui.subreddit.HomeViewer
import com.sofamaniac.reboost.ui.subreddit.MultiView
import com.sofamaniac.reboost.ui.subreddit.SubredditViewer
import com.sofamaniac.reboost.ui.subredditList.SubredditListViewer
import com.sofamaniac.reboost.ui.thread.ThreadView
import com.sofamaniac.reboost.ui.user.ProfileView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch


@HiltAndroidApp
class ReboostApp : Application()

val LocalTheme = compositionLocalOf<ReboostTheme> { DefaultReboostTheme }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProvideReboostTheme {
                val navController = rememberNavController()
                // Setup nav controller
                CompositionLocalProvider(LocalNavController provides navController) {
                    val theme = rememberAppTheme()
                    CompositionLocalProvider(LocalTheme provides theme) {
                        MainScreen(
                            navController = navController,
                        )
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerViewModel: DrawerViewModel = viewModel()

//    val fullScreenView by FullscreenManager.current.collectAsState(initial = null)
//    val fullScreenDepth by FullscreenManager.size.collectAsState(initial = 0)


    DisposableEffect(Unit) {
        onDispose {
            VideoPlayerManager.releasePlayer()
        }
    }

    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
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
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                viewModel = drawerViewModel,
                drawerState = drawerState,
            )
        },
    ) {
        NavigationGraph(
            navController,
            drawerState,
        )
    }


}

val BASE_URL = listOf(
    "https://reddit.com",
    "https://www.reddit.com",
    "https://old.reddit.com",
    "https://new.reddit.com",
    "http://reddit.com",
    "http://www.reddit.com",
    "http://old.reddit.com",
    "http://new.reddit.com",
)


inline fun <reified T : Any> makeDeepLinks(url: String): List<NavDeepLink> {
    return BASE_URL.map {
        navDeepLink<T>(basePath = "$it/$url")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    //val selected = remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selected = remember(navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route
        derivedStateOf {
            when {
                currentRoute?.contains(HomeRoute::class.qualifiedName ?: "") == true -> 0
                currentRoute?.contains(SearchRoute::class.qualifiedName ?: "") == true -> 1
                currentRoute?.contains(SubredditRoute::class.qualifiedName ?: "") == true -> 2
                currentRoute?.contains(SubscriptionsRoute::class.qualifiedName ?: "") == true -> 2
                currentRoute?.contains(MultiRoute::class.qualifiedName ?: "") == true -> 2
                currentRoute?.contains(InboxRoute::class.qualifiedName ?: "") == true -> 3
                currentRoute?.contains(ProfileRoute::class.qualifiedName ?: "") == true -> 4
                else -> {
                    Log.w("NavigationGraph", "Unknown route: $currentRoute")
                    0
                }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier.fillMaxSize(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<HomeRoute> {
            HomeViewer(
                drawerState, selected
            )
        }
        composable(
            route = PostRoute.routeString,
            deepLinks = makeDeepLinks<PostRoute>(url = "r/{subreddit}/comments/{id}/{title}"),
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
            ThreadView(permalink = permalink, dismiss = { navController.popBackStack() })
        }
        composable<SubscriptionsRoute> {
            SubredditListViewer(navController = navController)
        }
        composable<SearchRoute> { navBackStackEntry ->
            val search = navBackStackEntry.toRoute<SearchRoute>()
            SearchTab(search)
        }
        composable<InboxRoute> {
            SubredditViewer(
                "artknights",
                selected,
                drawerState,
            )
        }
        composable<SubredditRoute>(
            deepLinks = makeDeepLinks<SubredditRoute>(url = "r")
        )
        { navBackStackEntry ->
            val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
            SubredditViewer(
                subreddit,
                selected,
                drawerState,
            )
        }
        composable<MultiRoute> { navBackStackEntry ->
            val permalink = navBackStackEntry.toRoute<MultiRoute>().permalink
            val name = navBackStackEntry.toRoute<MultiRoute>().displayName
            MultiView(
                name,
                permalink,
                selected,
                drawerState,
            )
        }
        composable<ProfileRoute>(
            deepLinks = makeDeepLinks<ProfileRoute>(url = "u")
        ) { navBackStackEntry ->
            val params = navBackStackEntry.toRoute<ProfileRoute>()
            ProfileView(
                params.author,
                selected = selected,
                drawerState = rememberDrawerState(DrawerValue.Closed),
                isConnectedUser = params.isMe
            )
        }
        composable<LicensesRoute> {
            LicenseWebView()
        }
    }
}