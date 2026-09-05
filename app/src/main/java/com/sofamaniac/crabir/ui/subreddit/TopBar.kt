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
import com.sofamaniac.crabir.LocalFeedSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
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
import com.sofamaniac.crabir.ui.ThemedDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    params: FeedParams,
    slug: String,
    disableInfo: Boolean = false,
    updateSort: (Sort, Timeframe?) -> Unit,
    updateView: (Views) -> Unit,
    refresh: () -> Unit,
    openDrawer: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    entity: CommunityViewEntity,
) {
    val theme = LocalTheme.current
    val navController = LocalNavController.current

    var showViewSelect by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val viewSettingsStore = LocalContext.current.viewSettingDataStore
    val feedSettings = LocalFeedSettings.current
    fun updateViewInner(view: Views) {
        updateView(view)
        scope.launch {
            viewSettingsStore.updateData {
                if (it.rememberView) {
                    it.copy(
                        rememberedViews = it.rememberedViews + (slug to entity.copy(view = view))
                    )
                } else {
                    it
                }
            }
        }
    }

    fun updateSortOuter(sort: Sort, timeframe: Timeframe? = null) {
        updateSort(sort, timeframe)
        if (feedSettings.rememberSort) {
            scope.launch {
                viewSettingsStore.updateData {
                    it.copy(
                        rememberedViews = it.rememberedViews + (slug to entity.copy(
                            sort = sort,
                            timeframe = timeframe
                        ))
                    )
                }
            }
        }
    }


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
            IconButton(onClick = openDrawer) {
                Icon(
                    Icons.Default.Menu,
                    stringResource(R.string.open_drawer)
                )
            }
        },
        actions = {
            // Sort Dropdown
            var showMenu by remember { mutableStateOf(false) }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.MoreVert, stringResource(R.string.more_option_desc))
            }

            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(
                    onClick = {
                        showViewSelect = true
                    },
                    text = { Text(stringResource(R.string.change_post_view)) }
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
                        text = { Text(stringResource(R.string.go_to_sub_info)) }
                    )
                }
                DropdownMenuItem(
                    onClick = { refresh() },
                    text = { Text(stringResource(R.string.refresh)) })
            }
            if (!disableInfo) {
                IconButton(onClick = {
                    navController?.navigate(SubredditInfoRoute(slug))
                }) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            }
            SortMenu<Sort> { sort, timeframe ->
                updateSortOuter(sort, timeframe)
            }
        }
    )
    if (showViewSelect) {
        SelectViewDialog(
            onDismiss = { showViewSelect = false },
            selectedView = entity.view ?: LocalViewSettings.current.defaultView,
            updateView = ::updateViewInner
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectViewDialog(
    onDismiss: () -> Unit,
    selectedView: Views,
    updateView: (Views) -> Unit,
) {
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
    ThemedDialog(onDismiss) {
        Column(Modifier.selectableGroup()) {
            for (view in Views.entries) {
                ListItem(
                    selected = view == selectedView,
                    onClick = { selectOption(view) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = view == selectedView,
                            onClick = { selectOption(view) },
                            role = Role.RadioButton,
                        ),
                    content = { Text(stringResource(view.toStringResource())) },
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
