package com.sofamaniac.crabir.settings.feedSettings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.BackButton
import kotlinx.coroutines.launch

@Composable
fun FeedSettingsPage() {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val settingsStore = remember(context) { context.feedSettingsStore }
    val settings by settingsStore.data.collectAsState(FeedSettings())
    val scope = rememberCoroutineScope()
    fun update(transform: (FeedSettings) -> FeedSettings) {
        scope.launch {
            settingsStore.updateData(transform)
        }
    }
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.feeds_settings_title))
                    },
                    navigationIcon = {
                        BackButton {
                            navController?.popBackStack()
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    ListSelector(
                        leadingContent = { Icon(Icons.Default.Home, contentDescription = null) },
                        options = Sort.entries.toList(),
                        selectedOption = settings.homeSort,
                        optionLabel = {
                            stringResource(it.representation)
                        },
                        headlineContent = { Text(stringResource(R.string.home_sort_label)) }
                    ) { target ->
                        update {
                            val timeframe =
                                if (target.isTimeframe) (it.homeTimeframe
                                    ?: Timeframe.Day) else null
                            it.copy(homeSort = target, homeTimeframe = timeframe)
                        }
                    }
                }
                if (settings.homeSort.isTimeframe) {
                    item {
                        ListSelector(
                            options = Timeframe.entries.toList(),
                            selectedOption = settings.homeTimeframe,
                            optionLabel = {
                                stringResource(it?.representation ?: Timeframe.Day.representation)
                            },
                            headlineContent = { Text(stringResource(R.string.home_sort_timeframe_label)) }
                        ) { target ->
                            update {
                                it.copy(homeTimeframe = target)
                            }
                        }
                    }
                }
                item {
                    ListSelector(
                        leadingContent = {
                            Icon(
                                Icons.AutoMirrored.Filled.Sort,
                                contentDescription = null
                            )
                        },
                        options = Sort.entries.toList(),
                        selectedOption = settings.defaultSort,
                        optionLabel = {
                            stringResource(it.representation)
                        },
                        headlineContent = { Text(stringResource(R.string.feed_default_sort_label)) }
                    ) { target ->
                        update {
                            val timeframe =
                                if (target.isTimeframe) (it.defaultTimeframe
                                    ?: Timeframe.Day) else null
                            it.copy(defaultSort = target, defaultTimeframe = timeframe)
                        }
                    }
                }
                if (settings.defaultSort.isTimeframe) {
                    item {
                        ListSelector(
                            options = Timeframe.entries.toList(),
                            selectedOption = settings.defaultTimeframe,
                            optionLabel = {
                                stringResource(it?.representation ?: Timeframe.Day.representation)
                            },
                            headlineContent = { Text(stringResource(R.string.default_sort_timeframe_label)) }
                        ) { target ->
                            update {
                                it.copy(defaultTimeframe = target)
                            }
                        }
                    }
                }
                item {
                    SwitchTile(
                        headlineContent = { Text(stringResource(R.string.remember_sort_per_community)) },
                        supportingContent = { Text(stringResource(R.string.remember_sort_per_community_support)) },
                        checked = settings.rememberSort,
                        onCheckedChange = { target -> update { it.copy(rememberSort = target) } },
                        enabled = true
                    )
                }
                item {
                    val navController = LocalNavController.current
                    ListItem(
                        onClick = { navController?.navigate(SortManagerRoute) },
                        enabled = settings.rememberSort,
                        content = {
                            Text(stringResource(R.string.managed_remembered_sorts))
                        },
                    )
                }
            }
        }
    }
}
