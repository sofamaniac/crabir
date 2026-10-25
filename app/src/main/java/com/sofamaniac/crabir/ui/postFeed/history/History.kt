package com.sofamaniac.crabir.ui.postFeed.history

import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.reddit.HISTORY
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.SettingsPage
import com.sofamaniac.crabir.settings.helper.CheckboxTile
import com.sofamaniac.crabir.settings.theme.rememberTopAppBarColors
import com.sofamaniac.crabir.settings.views.Views
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.components.ThemedDialog
import com.sofamaniac.crabir.ui.postFeed.FullFeedView
import com.sofamaniac.crabir.ui.postFeed.ViewFull
import com.sofamaniac.crabir.ui.postFeed.components.OptionMenu
import com.sofamaniac.crabir.ui.postFeed.components.ViewEditDialog
import com.sofamaniac.crabir.ui.postFeed.defaultCommunityEntity
import com.sofamaniac.crabir.ui.postFeed.getCommunitySort
import com.sofamaniac.crabir.ui.postFeed.getCommunityView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryViewer(
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.History)
    val initialSort = getCommunitySort(HISTORY)
    val viewModel: HistoryViewModel = koinViewModel(key = HISTORY) {
        parametersOf(initialSort.sort, initialSort.timeframe)
    }
    val defaultView = getCommunityView(HISTORY)
    var entity by remember(HISTORY) { mutableStateOf(defaultView) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val accounts by viewModel.accounts.collectAsState()
    val selectedAccounts by viewModel.ignoredAccounts.collectAsState()

    LaunchedEffect(accounts) {
        viewModel.refresh()
    }

    val topBar = @Composable {
        TopBar(
            title,
            slug = HISTORY,
            currentView = entity,
            openDrawer = { scope.launch { drawerState.open() } },
            updateView = { entity = it },
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            defaultEntity = defaultCommunityEntity(HISTORY, title),
            accounts = accounts,
            selectedAccounts = selectedAccounts,
            updateSelected = { acc ->
                viewModel.updateIgnored(acc)
                viewModel.refresh()
            },
        )
    }
    val bottomBar = @Composable {
        TabBar(0, onTabReselect = {
            scope.launch {
                viewModel.listState.animateScrollToItem(0)
            }
        })
    }
    FullFeedView(
        topBar,
        bottomBar,
        viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        drawerState = drawerState,
        viewEntity = entity,
    )
}

@Composable
fun TopBar(
    title: String,
    slug: String,
    currentView: ViewFull,
    onInfoClick: (() -> Unit)? = null,
    updateView: (ViewFull) -> Unit,
    refresh: () -> Unit,
    openDrawer: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    defaultEntity: CommunityViewEntity,
    accounts: List<RedditAccount>,
    selectedAccounts: Set<Fullname>,
    updateSelected: (Set<Fullname>) -> Unit,
) {
    val navController = LocalNavController.current

    var showViewSelect by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val viewSettingsStore = LocalContext.current.viewSettingDataStore

    fun updateViewInner(view: Views) {
        updateView(currentView.copy(view = view))
        scope.launch {
            viewSettingsStore.updateData {
                if (it.rememberView) {
                    val old = it.rememberedViews[slug] ?: defaultEntity
                    it.copy(
                        rememberedViews = it.rememberedViews + (slug to old.copy(view = view))
                    )
                } else {
                    it
                }
            }
        }
    }

    fun updateColumns(columns: Int) {
        updateView(currentView.copy(columns = columns))
        scope.launch {
            viewSettingsStore.updateData {
                if (it.rememberView) {
                    val old = it.rememberedViews[slug] ?: defaultEntity
                    it.copy(
                        rememberedViews = it.rememberedViews + (slug to old.copy(columns = columns))
                    )
                } else {
                    it
                }
            }
        }
    }
    LaunchedEffect(title) {
        scope.launch {
            viewSettingsStore.updateData {
                if (it.rememberView) {
                    val old = it.rememberedViews[slug] ?: defaultEntity
                    it.copy(
                        rememberedViews = it.rememberedViews + (slug to old.copy(displayName = title))
                    )
                } else {
                    it
                }
            }
        }
    }


    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = rememberTopAppBarColors(),
        title = {
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
            OptionMenu(
                onPostViewClick = { showViewSelect = true },
                onSettingsClick = { navController?.navigate(SettingsPage()) },
                refresh = refresh,
                onInfoClick = onInfoClick,
            )
            if (onInfoClick != null) {
                IconButton(onClick = onInfoClick) {
                    Icon(Icons.Outlined.Info, contentDescription = null)
                }
            }
            AccountsSelector(accounts, selectedAccounts, updateSelected)
        }
    )
    if (showViewSelect) {
        ViewEditDialog(
            onDismiss = { showViewSelect = false },
            selectedView = currentView.view,
            updateView = ::updateViewInner,
            selectedColumns = currentView.columns,
            updateColumns = ::updateColumns
        )
    }
}

@Composable
private fun AccountsSelector(
    accounts: List<RedditAccount>,
    ignoredAccounts: Set<Fullname>,
    updateSelected: (Set<Fullname>) -> Unit,
) {
    var ignoredAccounts by remember { mutableStateOf(ignoredAccounts) }
    var showDialog by remember { mutableStateOf(false) }
    val tristate = remember(ignoredAccounts, accounts) {
        if (ignoredAccounts.isEmpty()) {
            ToggleableState.On
        } else if (accounts.any { it.name !in ignoredAccounts }) {
            ToggleableState.Indeterminate
        } else {
            ToggleableState.Off
        }
    }
    IconButton(onClick = { showDialog = true }) {
        Icon(
            Icons.Default.FilterAlt,
            contentDescription = stringResource(R.string.history_filter_desc)
        )
    }
    if (showDialog) {
        ThemedDialog(
            onDismissRequest = { showDialog = false },
            confirm = {
                TextButton(onClick = {
                    updateSelected(ignoredAccounts)
                    showDialog = false
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            cancel = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            ListItem(
                modifier = Modifier.triStateToggleable(
                    state = tristate,
                    role = Role.Checkbox,
                    onClick = {
                        ignoredAccounts = when (tristate) {
                            ToggleableState.Off, ToggleableState.Indeterminate -> emptySet()
                            ToggleableState.On -> accounts.map { it.name }.toSet()
                        }
                    }
                ),
                content = {
                    Text(stringResource(R.string.history_accounts_dialog))
                },
                trailingContent = {
                    TriStateCheckbox(state = tristate, onClick = null)
                }
            )
            for (account in accounts.filter { it.info != null }) {
                CheckboxTile(
                    headlineContent = { Text(account.info!!.username) },
                    checked = account.name !in ignoredAccounts,
                    onCheckedChange = {
                        ignoredAccounts = if (it) {
                            ignoredAccounts - account.name
                        } else {
                            ignoredAccounts + account.name
                        }
                    },
                )
            }
            val anonymous = RedditAccount.anonymous()
            CheckboxTile(
                headlineContent = { Text("Anonymous") },
                checked = anonymous.name !in ignoredAccounts,
                onCheckedChange = {
                    ignoredAccounts = if (it) {
                        ignoredAccounts - anonymous.name
                    } else {
                        ignoredAccounts + anonymous.name
                    }
                },
            )
        }
    }
}
