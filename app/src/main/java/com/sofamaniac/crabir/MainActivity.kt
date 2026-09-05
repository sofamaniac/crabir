/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:14 PM
 *
 */

package com.sofamaniac.crabir

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.memoryCacheMaxSizePercentWhileInBackground
import coil3.util.DebugLogger
import com.sofamaniac.crabir.navigation.HomeRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.NavigationGraph
import com.sofamaniac.crabir.settings.ConfigureSettings
import com.sofamaniac.crabir.settings.theme.ConfigureCrabirTheme
import com.sofamaniac.crabir.ui.media.videoPlayer.VideoPlayerManager
import com.sofamaniac.crabir.ui.rememberCurrentAccount
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.option.viewModelScopeFactory
import org.koin.plugin.module.dsl.startKoin


@KoinApplication
class CrabirApp : Application() {
    @OptIn(KoinViewModelScopeApi::class)
    override fun onCreate() {
        super.onCreate()
        startKoin<CrabirApp> {
            //androidLogger(Level.DEBUG)
            androidContext(this@CrabirApp)
            options(viewModelScopeFactory())
        }
    }
}


class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    lateinit var uriHandler: UriHandler

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        Log.d("MainActivity", "handleIntent: $intent")
        intent.data?.let { uri ->
            val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
            try {
                navController.navigate(
                    request = request,
                    navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
                )
            } catch (_: IllegalArgumentException) {
                try {
                    if (uri.scheme == "http" || uri.scheme == "https") {
                        uriHandler.openUri(uri.toString())
                    }
                } catch (e: IllegalArgumentException) {
                    Log.e("MainActivity", "handleIntent: failed to open uri: $uri", e)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashOnScreen = true
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        VideoPlayerManager.initialize(this)
        enableEdgeToEdge()
        setContent {

            navController = rememberNavController()
            uriHandler = LocalUriHandler.current
            setImageLoader()
            MainScreen(navController = navController) {
                keepSplashOnScreen = false
                handleIntent(intent)
            }
        }
    }
}

@Immutable
data class CrabirUriHandler(val navController: NavController?, val fallback: UriHandler) :
    UriHandler {
    override fun openUri(uri: String) {
        try {
            val request = NavDeepLinkRequest.Builder.fromUri(uri.toUri()).build()
            navController?.navigate(request)
        } catch (_: IllegalArgumentException) {
            Log.i("CrabirUriHandler", "could not open: $uri")
            fallback.openUri(uri)
        }
    }

}

@SuppressLint("ComposableNaming")
@Composable
fun setImageLoader() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context).memoryCache {
            MemoryCache.Builder().maxSizePercent(context, 0.25)
                .build()
        }.diskCache {
            // 2go
            val size: Long = 2L * 1024L * 1024L * 1024L
            DiskCache.Builder()
                .maxSizeBytes(size)
                .directory(context.cacheDir.resolve("image_cache")).build()
        }
            .memoryCacheMaxSizePercentWhileInBackground(0.10)
            .let { builder -> if (BuildConfig.DEBUG) builder.logger(DebugLogger()) else builder }
            .build()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onLoad: () -> Unit,
) {

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val player = VideoPlayerManager.getInstance()
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> player.play()
                Lifecycle.Event.ON_DESTROY -> VideoPlayerManager.releasePlayer()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            Log.d("MainScreen", "onDispose")
            lifecycle.removeObserver(observer)
        }
    }
    val currentAccount = rememberCurrentAccount()
    ConfigureCrabirTheme {
        ConfigureSettings {
            CompositionLocalProvider(
                LocalNavController provides navController,
                LocalRedditAccount provides currentAccount,
            ) {
                val clientId = LocalApiSettings.current.redditClientId
                LaunchedEffect(currentAccount) {
                    if (!currentAccount.isUninitialized() && !clientId.isNullOrBlank()) {
                        navController.navigate(HomeRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    onLoad()
                }
                SetShortcuts()
                NavigationGraph(
                    navController,
                )
            }
        }
    }
}


