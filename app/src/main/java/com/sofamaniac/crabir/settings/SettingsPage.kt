package com.sofamaniac.crabir.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.interceptors.CountInterceptor
import com.sofamaniac.crabir.navigation.DebugOptionsRoute
import com.sofamaniac.crabir.navigation.FiltersSettingRoute
import com.sofamaniac.crabir.navigation.LicensesRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ThemeRoute
import com.sofamaniac.crabir.navigation.ViewsSettingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
    val navController = LocalNavController.current!!
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ListItem(
                headlineContent = { Text("Theme") },
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = "Theme") },
                modifier = Modifier.clickable {
                    navController.navigate(ThemeRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Views") },
                leadingContent = { Icon(Icons.Default.ViewComfy, contentDescription = "Views") },
                modifier = Modifier.clickable {
                    navController.navigate(ViewsSettingRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Filters") },
                leadingContent = { Icon(Icons.Default.FilterList, contentDescription = "Filters") },
                modifier = Modifier.clickable {
                    navController.navigate(FiltersSettingRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Licenses") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = "Licenses") },
                modifier = Modifier.clickable {
                    navController.navigate(LicensesRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Dev Options") },
                leadingContent = { Icon(Icons.Default.BugReport, contentDescription = null) },
                modifier = Modifier.clickable {
                    navController.navigate(DebugOptionsRoute)
                }
            )
        }
    }
}


@Composable
internal fun DebugOptionsView(viewModel: DebugOptionViewModel = hiltViewModel()) {
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

@HiltViewModel
internal class DebugOptionViewModel @Inject constructor(
    private val visitedPostsDao: VisitedPostsDao
) : ViewModel() {
    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.clearAll()
        }
    }
}