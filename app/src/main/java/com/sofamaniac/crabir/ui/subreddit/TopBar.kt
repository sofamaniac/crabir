package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SettingsRoute
import com.sofamaniac.crabir.navigation.SubredditInfoRoute
import com.sofamaniac.crabir.settings.views.ViewSettings
import com.sofamaniac.crabir.settings.views.Views
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import com.sofamaniac.crabir.ui.SortMenu
import com.sofamaniac.crabir.ui.ThemedCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    params: FeedParams,
    slug: String,
    disableInfo: Boolean = false,
    updateSort: (Sort, Timeframe?) -> Unit,
    refresh: () -> Unit,
    openDrawer: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    view: Views,
    updateView: (Views) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val theme = LocalTheme.current
    val navController = LocalNavController.current

    var showViewSelect by remember { mutableStateOf(false) }

    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = theme.toolbarBackground,
            scrolledContainerColor = theme.toolbarBackground,
            titleContentColor = theme.toolbarText,
        ),
        title = {
            Column {
                Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val sortString = stringResource(params.sort.representation)
                    val timeString = params.timeframe?.let { stringResource(it.representation) }
                    val fullString = if (params.timeframe != null) {
                        stringResource(R.string.sort_timeframe, sortString, timeString!!)
                    } else {
                        sortString
                    }
                    Text(fullString, style = MaterialTheme.typography.labelSmall)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) { Icon(Icons.Default.Menu, "Open Drawer") }
        },
        actions = {
            // Sort Dropdown
            var showMenu by remember { mutableStateOf(false) }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.MoreVert, "Options")
            }

            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(
                    onClick = {
                        showViewSelect = true
                    },
                    text = { Text("Post view") }
                )
                DropdownMenuItem(
                    onClick = {
                        navController?.navigate(SettingsRoute)
                    },
                    text = { Text(stringResource(R.string.settings)) }
                )
                if (!disableInfo) {
                    DropdownMenuItem(
                        onClick = {
                            navController?.navigate(SubredditInfoRoute(slug))
                        },
                        text = { Text("Info") }
                    )
                }
                DropdownMenuItem(onClick = { refresh() }, text = { Text("Refresh") })
            }
            if (!disableInfo) {
                IconButton(onClick = {
                    navController?.navigate(SubredditInfoRoute(slug))
                }) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            }
            SortMenu<Sort> { sort, timeframe ->
                updateSort(sort, timeframe)
            }
        }
    )
    if (showViewSelect) {
        SelectViewDialog(
            onDismiss = { showViewSelect = false },
            selectedView = view,
            updateView = updateView
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectViewDialog(onDismiss: () -> Unit, selectedView: Views, updateView: (Views) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewSettingDataStore = remember(context) { context.viewSettingDataStore }
    val viewSettings by viewSettingDataStore.data.collectAsState(ViewSettings())
    val useDefault = !viewSettings.rememberView
    fun selectOption(view: Views) {
        if (useDefault) {
            scope.launch {
                viewSettingDataStore.updateData { it.copy(defaultView = view) }
            }
        } else {
            updateView(view)
        }
    }

    val selectedView = if (useDefault) viewSettings.defaultView else selectedView
    BasicAlertDialog(onDismiss) {
        ThemedCard {
            Column(Modifier.selectableGroup()) {
                for (view in Views.entries) {
                    ListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = view == selectedView,
                                onClick = { selectOption(view) },
                                role = Role.RadioButton,
                            ),
                        headlineContent = { Text(stringResource(view.toStringResource())) },
                        trailingContent = {
                            RadioButton(
                                selected = view == selectedView,
                                onClick = null
                            )
                        }
                    )
                }
            }
        }
    }
}