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
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sofamaniac.reboost.ui.drawer.DrawerContent
import com.sofamaniac.reboost.ui.drawer.DrawerViewModel
import com.sofamaniac.reboost.ui.media.videoPlayer.VideoPlayerManager
import com.sofamaniac.reboost.ui.subreddit.HomeViewer
import com.sofamaniac.reboost.ui.subreddit.MultiView
import com.sofamaniac.reboost.ui.subreddit.SubredditViewer
import com.sofamaniac.reboost.ui.subredditList.SubredditListViewer
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import com.sofamaniac.reboost.ui.thread.ThreadView
import com.sofamaniac.reboost.ui.user.ProfileView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


@HiltAndroidApp
class ReboostApp : Application()

interface Tab {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?)

    @Composable
    fun Content(
        navController: NavController,
        selected: MutableIntState,
        modifier: Modifier = Modifier
    )
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReboostTheme {
                MaterialTheme {
                    val navController = rememberNavController()
                    // Setup nav controller
                    CompositionLocalProvider(LocalNavController provides navController) {
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

    val fullScreenView by FullscreenManager.current.collectAsState(initial = null)


    DisposableEffect(Unit) {
        onDispose {
            VideoPlayerManager.releasePlayer()
        }
    }

    Box {
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

        fullScreenView?.invoke()
    }

}

object FullscreenManager {
    private var _fullscreenViews = MutableStateFlow(emptyList<@Composable () -> Unit>())

    val current: Flow<@Composable (() -> Unit)?>
        get() = _fullscreenViews.map { it.lastOrNull() }.distinctUntilChanged()


    fun push(view: @Composable () -> Unit) {
        _fullscreenViews.update {
            it + view
        }
    }

    fun pop() {
        _fullscreenViews.update {
            it.dropLast(1)
        }
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
        composable<PostRoute>(
            enterTransition = {
                fadeIn(animationSpec = tween(500)) +
                        slideIn(animationSpec = tween(500)) { fullSize ->
                            IntOffset(fullSize.width, 0)
                        }
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500)) +
                        slideOut(animationSpec = tween(500)) { fullSize ->
                            IntOffset(fullSize.width, 0)
                        }
            },
        )
        {
            ThreadView(dismiss = { navController.popBackStack() })
        }
        composable<SubscriptionsRoute> {
            SubredditListViewer(navController = navController)
        }
        composable<SearchRoute> {
            SubredditViewer(
                "artknights",
                selected,
                drawerState,
            )
        }
        composable<InboxRoute> {
            SubredditViewer(
                "artknights",
                selected,
                drawerState,
            )
        }
        composable<SubredditRoute> { navBackStackEntry ->
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
        composable<ProfileRoute> { navBackStackEntry ->
            val user = navBackStackEntry.toRoute<ProfileRoute>().author
            ProfileView(
                user,
                selected = selected,
                drawerState = rememberDrawerState(DrawerValue.Closed)
            )
        }
        composable<LicensesRoute> {
            LicenseWebView()
        }
    }
}