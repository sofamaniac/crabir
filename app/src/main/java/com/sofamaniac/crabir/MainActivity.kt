/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:14 PM
 *
 */

package com.sofamaniac.crabir

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.rememberCurrentAccount
import com.sofamaniac.crabir.navigation.HistoryRoute
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.InboxRoute
import com.sofamaniac.crabir.navigation.LicensesRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.navigation.SettingsRoute
import com.sofamaniac.crabir.navigation.SubscriptionsRoute
import com.sofamaniac.crabir.navigation.ThemeEditorRoute
import com.sofamaniac.crabir.navigation.ThemeRoute
import com.sofamaniac.crabir.navigation.ViewsSettingRoute
import com.sofamaniac.crabir.navigation.imagesGraph
import com.sofamaniac.crabir.navigation.postGraph
import com.sofamaniac.crabir.navigation.profileGraph
import com.sofamaniac.crabir.navigation.subredditGraph
import com.sofamaniac.crabir.settings.SettingsPage
import com.sofamaniac.crabir.settings.theme.ConfigureMaterialTheme
import com.sofamaniac.crabir.settings.theme.CrabirTheme
import com.sofamaniac.crabir.settings.theme.DefaultDarkTheme
import com.sofamaniac.crabir.settings.theme.ThemeEditor
import com.sofamaniac.crabir.settings.theme.ThemeSettingsPage
import com.sofamaniac.crabir.settings.theme.rememberAppTheme
import com.sofamaniac.crabir.settings.views.ViewsSettingsPage
import com.sofamaniac.crabir.ui.InboxView
import com.sofamaniac.crabir.ui.VerticalSwipeToDismiss
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayerManager
import com.sofamaniac.crabir.ui.search.SearchTab
import com.sofamaniac.crabir.ui.subreddit.HistoryViewer
import com.sofamaniac.crabir.ui.subreddit.HomeViewer
import com.sofamaniac.crabir.ui.subredditList.SubredditListViewer
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage


@HiltAndroidApp
class CrabirApp : Application()

val LocalTheme = compositionLocalOf<CrabirTheme> { DefaultDarkTheme }
val LocalDrawerState = compositionLocalOf<DrawerState> { error("No drawer state provided") }
val LocalRedditAccount = compositionLocalOf<RedditAccount> { RedditAccount.anonymous() }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    lateinit var uriHandler: UriHandler

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent: $intent")
        intent.data?.let { uri ->
            val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
            try {
                navController.navigate(
                    request = request,
                    navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
                )
            } catch (e: IllegalArgumentException) {
                uriHandler.openUri(uri.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {

            navController = rememberNavController()
            uriHandler = LocalUriHandler.current

            // Setup nav controller
            CompositionLocalProvider(LocalNavController provides navController) {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                ConfigureMaterialTheme {
                    val theme = rememberAppTheme()
                    if (theme == null) {
                        return@ConfigureMaterialTheme
                    }
                    CompositionLocalProvider(LocalTheme provides theme) {
                        CompositionLocalProvider(LocalDrawerState provides drawerState) {
                            val currentAccount = rememberCurrentAccount()
                            CompositionLocalProvider(LocalRedditAccount provides currentAccount) {
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
                Log.d("MainScreen", "Popping back stack ${navController.currentBackStackEntry}")
                navController.popBackStack()
            } else {
                activity?.finish()
            }
        }
    }

    NavigationGraph(
        navController,
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = modifier
                .fillMaxSize()
                .imePadding(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<HomeRoute> {
                Log.d("NavigationGraph", "HomeRoute")
                HomeViewer()
            }

            profileGraph(navController = navController)
            postGraph(navController = navController)
            subredditGraph(navController = navController)
            imagesGraph(navController = navController)

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

            composable(
                route = "videoPreview?url={url}",
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "v.redd.it/{url}"
                    }
                ),
                arguments = listOf(
                    navArgument("url") {
                        type = NavType.StringType
                    },
                )
            ) { navBackStackEntry ->
                val url = navBackStackEntry.arguments?.getString("url")
                if (url != null) {
                    VerticalSwipeToDismiss(
                        onDismiss = {
                            // Why do we need to pop twice?
                            navController.popBackStack()
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black)
                    ) {
                        ZoomableAsyncImage(
                            model = "https://v.redd.it/$url",
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

