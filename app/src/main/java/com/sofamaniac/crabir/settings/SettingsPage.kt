package com.sofamaniac.crabir.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.sofamaniac.crabir.settings.data.DataSettingsRoute
import com.sofamaniac.crabir.settings.post.PostSettingsRoute
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
                title = { Text("Settings") },
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
        Column(modifier = Modifier.padding(padding)) {
            ListItem(
                headlineContent = { Text("General") },
                modifier = Modifier.clickable {
                    navController?.navigate(GeneralSettingsRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Theme") },
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(ThemeRoute)
                }
            )

            ListItem(
                headlineContent = { Text("Filters") },
                leadingContent = { Icon(Icons.Default.FilterList, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(FiltersSettingRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Data") },
                leadingContent = { Icon(Icons.Default.DataUsage, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(DataSettingsRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Licenses") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController?.navigate(LicensesRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Dev Options") },
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
                title = { Text("Settings") },
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
                ListItem(headlineContent = { Text("Posts") }, modifier = Modifier.clickable {
                    navController?.navigate(PostSettingsRoute)
                })
            }
            item {
                ListItem(headlineContent = { Text("Comments") }, modifier = Modifier.clickable {
                })
            }
            item {
                ListItem(headlineContent = { Text("Views") }, modifier = Modifier.clickable {
                    navController?.navigate(ViewsSettingRoute)
                })
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
                    headlineContent = { Text("Number of request this session") },
                    supportingContent = { Text(CountInterceptor.count.toString()) }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text("Clear history") },
                    modifier = Modifier.clickable {
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