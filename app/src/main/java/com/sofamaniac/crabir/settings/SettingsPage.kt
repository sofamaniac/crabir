package com.sofamaniac.crabir.settings

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.interceptors.CountInterceptor
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.SettingsRoute
import com.sofamaniac.crabir.settings.api.ApiSettingsPage
import com.sofamaniac.crabir.settings.comments.CommentsSettingsPage
import com.sofamaniac.crabir.settings.data.DataSettingsPage
import com.sofamaniac.crabir.settings.feedSettings.FeedSettingsPage
import com.sofamaniac.crabir.settings.filters.FiltersSettingsPage
import com.sofamaniac.crabir.settings.history.HistorySettingsPage
import com.sofamaniac.crabir.settings.lateralMenu.LateralMenuSettingsPage
import com.sofamaniac.crabir.settings.post.PostSettingsPage
import com.sofamaniac.crabir.settings.theme.ThemeEditor
import com.sofamaniac.crabir.settings.theme.ThemeSettingsPage
import com.sofamaniac.crabir.settings.views.ViewsSettingsPage
import com.sofamaniac.crabir.ui.components.BackButton
import com.sofamaniac.crabir.ui.components.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

data class SettingsPageItem(
    @param:StringRes val title: Int,
    val route: SettingsRoute,
    val icon: ImageVector,
)

fun constructSettingsItems(): List<SettingsPageItem> {
    return buildList {
        add(
            SettingsPageItem(
                R.string.general_settings_title,
                SettingsRoute.General.Main,
                Icons.Default.Settings
            )
        )
        add(
            SettingsPageItem(
                R.string.theme_settings_title,
                SettingsRoute.Theme.Main,
                Icons.Default.Palette
            )
        )
        add(
            SettingsPageItem(
                R.string.filter_settings_name,
                SettingsRoute.Filters,
                Icons.Default.FilterList
            )
        )
        add(
            SettingsPageItem(
                R.string.data_settings_name,
                SettingsRoute.Data,
                Icons.Default.DataUsage
            )
        )
        add(
            SettingsPageItem(
                R.string.history_settings_tile,
                SettingsRoute.History,
                Icons.Default.History
            )
        )
        add(
            SettingsPageItem(
                R.string.api_settings_tile,
                SettingsRoute.Api,
                Icons.Default.Api
            )
        )
        if (BuildConfig.DEBUG) {
            add(
                SettingsPageItem(
                    R.string.debug_options_tile,
                    SettingsRoute.DebugOptions,
                    Icons.Default.BugReport
                )
            )
        }
        add(
            SettingsPageItem(
                R.string.licenses,
                SettingsRoute.Licenses,
                Icons.Outlined.Info
            )
        )

    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsPageAdaptive(route: SettingsRoute?) {
    val navigator = rememberListDetailPaneScaffoldNavigator<SettingsRoute>()
    val scope = rememberCoroutineScope()
    val settingsItems = constructSettingsItems()
    LaunchedEffect(route) {
        when (route) {
            is SettingsRoute.ExtraRoute -> {
                navigator.navigateTo(ListDetailPaneScaffoldRole.Extra, route)
            }

            is SettingsRoute.DetailsRoute -> {
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, route)
            }

            else -> {}
        }
    }
    val navController = LocalNavController.current
    Surface(modifier = Modifier.fillMaxSize()) {
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                AnimatedPane {
                    val selected = navigator.currentDestination?.contentKey
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(stringResource(R.string.settings_title)) },
                                navigationIcon = {
                                    BackButton { navController?.popBackStack() }
                                }
                            )
                        },
                    ) { padding ->
                        LazyColumn(
                            modifier = Modifier.padding(padding),
                        ) {
                            items(settingsItems.size) { index ->
                                val item = settingsItems[index]
                                ListItem(
                                    selected = selected == item.route,
                                    leadingContent = { Icon(item.icon, contentDescription = null) },
                                    content = { Text(stringResource(item.title)) },
                                    onClick = {
                                        scope.launch {
                                            navigator.navigateTo(
                                                ListDetailPaneScaffoldRole.Detail,
                                                item.route
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            },
            detailPane = {
                AnimatedPane() {
                    val route = navigator.currentDestination?.contentKey
                    if (route !is SettingsRoute.DetailsRoute) {
                        Box(modifier = Modifier)
                    } else {
                        SettingsDetailPane(
                            route,
                            {
                                scope.launch {
                                    navigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Extra,
                                        it
                                    )
                                }
                            },
                            {
                                scope.launch {
                                    navigator.navigateBack(
                                        BackNavigationBehavior.PopUntilScaffoldValueChange
                                    )
                                }
                            }
                        )
                    }
                }
            },
            extraPane = {
                val route = navigator.currentDestination?.contentKey
                AnimatedPane() {
                    if (route !is SettingsRoute.ExtraRoute) return@AnimatedPane
                    SettingsExtraPane(
                        route,
                        {
                            scope.launch {
                                navigator.navigateBack(
                                    BackNavigationBehavior.PopUntilScaffoldValueChange
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun SettingsDetailPane(
    route: SettingsRoute.DetailsRoute,
    navigateTo: (SettingsRoute) -> Unit,
    navigateBack: () -> Unit,
) {
    when (route) {
        SettingsRoute.Api -> ApiSettingsPage(navigateBack)
        SettingsRoute.Data -> DataSettingsPage(navigateBack)
        SettingsRoute.DebugOptions -> DebugOptionsView()
        SettingsRoute.Filters -> FiltersSettingsPage(navigateBack)
        is SettingsRoute.General -> GeneralSettingsPage(route, navigateTo, navigateBack)
        SettingsRoute.History -> HistorySettingsPage(navigateBack)
        SettingsRoute.Licenses -> LicensePage(navigateBack)
        is SettingsRoute.Theme -> ThemeSettingsPage(navigateTo, navigateBack)
    }
}

@Composable
fun SettingsExtraPane(route: SettingsRoute.ExtraRoute, navigateBack: () -> Unit) {
    when (route) {
        is SettingsRoute.General -> GeneralExtraPane(route as SettingsRoute.General, navigateBack)
        SettingsRoute.Theme.Editor -> ThemeEditor(navigateBack)
    }

}

@Composable
fun GeneralExtraPane(
    route: SettingsRoute.General,
    navigateBack: () -> Unit,
) {
    when (route) {
        SettingsRoute.General.Feeds -> FeedSettingsPage(navigateBack)
        SettingsRoute.General.Posts -> PostSettingsPage(navigateBack)
        SettingsRoute.General.Views -> ViewsSettingsPage(navigateBack)
        SettingsRoute.General.Comments -> CommentsSettingsPage(navigateBack)
        SettingsRoute.General.LateralMenu -> LateralMenuSettingsPage(navigateBack)
        SettingsRoute.General.Main -> throw IllegalStateException("route is ExtraRoute")
    }

}

@Composable
fun GeneralSettingsPage(
    selected: SettingsRoute.General,
    navigateTo: (SettingsRoute) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    BackButton { navigateBack() }
                }
            )
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                ListItem(
                    selected = selected == SettingsRoute.General.Feeds,
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.feeds_settings_tile_label)) },
                    onClick = {
                        navigateTo(SettingsRoute.General.Feeds)
                    }
                )
            }
            item {
                ListItem(
                    selected = selected == SettingsRoute.General.Posts,
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.Article,
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.posts_settings_name)) },
                    onClick = {
                        navigateTo(SettingsRoute.General.Posts)
                    }
                )
            }
            item {
                ListItem(
                    selected = selected == SettingsRoute.General.Comments,
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.Comment,
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.comments_settings_title)) },
                    onClick = {
                        navigateTo(SettingsRoute.General.Comments)
                    }
                )
            }
            item {
                ListItem(
                    selected = selected == SettingsRoute.General.Views,
                    leadingContent = { Icon(Icons.Default.ViewComfy, contentDescription = null) },
                    content = { Text(stringResource(R.string.views_settings_title)) },
                    onClick = {
                        navigateTo(SettingsRoute.General.Views)
                    }
                )
            }
            item {
                ListItem(
                    selected = selected == SettingsRoute.General.LateralMenu,
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.side_navigation),
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.lateral_menu)) },
                    onClick = {
                        navigateTo(SettingsRoute.General.LateralMenu)
                    }
                )
            }
        }
    }
}


@SuppressLint("HardcodedComposeText")
@Composable
internal fun DebugOptionsView(viewModel: DebugOptionViewModel = koinViewModel()) {
    Scaffold { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                ListItem(
                    content = { Text("Number of request this session") },
                    supportingContent = { Text(CountInterceptor.count.toString()) }
                )
            }
            item {
                ListItem(
                    content = { Text("Clear history") },
                    onClick = {
                        viewModel.clearHistory()
                    })
            }
        }
    }
}

@KoinViewModel
internal class DebugOptionViewModel(
    private val visitedPostsDao: VisitedPostsDao,
) : ViewModel() {
    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.clearAll()
        }
    }
}

@Composable
fun LicensePage(navigateBack: () -> Unit) {
    val libraries by produceLibraries(R.raw.aboutlibraries)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.licenses)) }, navigationIcon = {
                BackButton { navigateBack() }
            })
        }
    ) { padding ->
        LibrariesContainer(
            libraries, modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}
