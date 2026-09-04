package com.sofamaniac.crabir.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.DevicesFold
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.interceptors.CountInterceptor
import com.sofamaniac.crabir.navigation.DebugOptionsRoute
import com.sofamaniac.crabir.navigation.FiltersSettingRoute
import com.sofamaniac.crabir.navigation.LicensesRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.navigation.ThemeRoute
import com.sofamaniac.crabir.navigation.ViewsSettingRoute
import com.sofamaniac.crabir.settings.api.ApiSettingsRoute
import com.sofamaniac.crabir.settings.comments.CommentsSettingsRoute
import com.sofamaniac.crabir.settings.data.DataSettingsRoute
import com.sofamaniac.crabir.settings.feedSettings.FeedSettingsRoute
import com.sofamaniac.crabir.settings.lateralMenu.LateralMenuSettingsRoute
import com.sofamaniac.crabir.settings.post.PostSettingsRoute
import com.sofamaniac.crabir.ui.BackButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back
                            )
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Default.Settings, contentDescription = null) },
                content = { Text(stringResource(R.string.general_settings_title)) },
                modifier = Modifier.clickable {
                    navController?.navigate(GeneralSettingsRoute)
                }
            )
            ListItem(
                content = { Text(stringResource(R.string.theme_settings_title)) },
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(ThemeRoute)
                }
            )

            ListItem(
                content = { Text(stringResource(R.string.filter_settings_name)) },
                leadingContent = { Icon(Icons.Default.FilterList, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(FiltersSettingRoute)
                }
            )
            ListItem(
                content = { Text(stringResource(R.string.data_settings_name)) },
                leadingContent = { Icon(Icons.Default.DataUsage, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(DataSettingsRoute)
                }
            )
            ListItem(
                content = { Text(stringResource(R.string.licenses)) },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(LicensesRoute)
                }
            )
            ListItem(
                content = { Text(stringResource(R.string.api_settings_tile)) },
                leadingContent = { Icon(Icons.Default.Api, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(ApiSettingsRoute)
                }
            )
            ListItem(
                content = { Text("Dev Options") },
                leadingContent = { Icon(Icons.Default.BugReport, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(DebugOptionsRoute)
                }
            )
        }
    }
}

@Serializable
object GeneralSettingsRoute : Route

@Composable
fun GeneralSettingsPage() {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back
                            )
                        )
                    }
                }
            )
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.feeds_settings_tile_label)) },
                    onClick = {
                        navController?.navigate(FeedSettingsRoute)
                    }
                )
            }
            item {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.Article,
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.posts_settings_name)) },
                    onClick = {
                        navController?.navigate(PostSettingsRoute)
                    }
                )
            }
            item {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.Comment,
                            contentDescription = null
                        )
                    },
                    content = { Text(stringResource(R.string.comments_settings_title)) },
                    onClick = {
                        navController?.navigate(CommentsSettingsRoute)
                    }
                )
            }
            item {
                ListItem(
                    leadingContent = { Icon(Icons.Default.ViewComfy, contentDescription = null) },
                    content = { Text(stringResource(R.string.views_settings_title)) },
                    onClick = {
                        navController?.navigate(ViewsSettingRoute)
                    }
                )
            }
            item {
                ListItem(
                    leadingContent = { Icon(Icons.Default.DevicesFold, contentDescription = null) },
                    content = { Text(stringResource(R.string.lateral_menu)) },
                    onClick = {
                        navController?.navigate(LateralMenuSettingsRoute)
                    }
                )
            }
        }
    }
}


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
fun LicensePage() {
    val libraries by produceLibraries(R.raw.aboutlibraries)
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.licenses)) }, navigationIcon = {
                BackButton { navController?.popBackStack() }
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