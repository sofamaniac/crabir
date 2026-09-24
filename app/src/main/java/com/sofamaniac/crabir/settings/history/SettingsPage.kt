package com.sofamaniac.crabir.settings.history

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.components.ThemedDialog
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun HistorySettingsPage(navigateBack: () -> Unit) {
    val context = LocalContext.current
    val store = context.historySettingsDataStore
    val settings by store.data.collectAsState(initial = HistorySettings())
    val scope = rememberCoroutineScope()

    fun update(newValue: HistorySettings) {
        scope.launch {
            store.updateData { newValue }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_settings_title)) },
                navigationIcon = {
                    BackButton { navigateBack() }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.history_enabled)) },
                    onCheckedChange = { target ->
                        update(settings.copy(enabled = target))
                    },
                    checked = settings.enabled,
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.history_save_nsfw)) },
                    onCheckedChange = { target ->
                        update(settings.copy(saveNSFW = target))
                    },
                    checked = settings.saveNSFW
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.history_dim_images)) },
                    onCheckedChange = { target ->
                        update(settings.copy(dimImages = target))
                    },
                    checked = settings.dimImages
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.history_read_on_scroll)) },
                    onCheckedChange = { target ->
                        update(settings.copy(readOnScroll = target))
                    },
                    checked = settings.readOnScroll
                )
            }
            item {
                ClearHistoryButton()
            }
        }
    }
}

@Composable
private fun ClearHistoryButton(historyManager: HistoryManager = koinInject()) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    ListItem(
        onClick = { showConfirmationDialog = true },
        leadingContent = { Spacer(Modifier.size(24.dp)) }
    ) {
        Text(stringResource(R.string.clear_history))
    }
    val scope = rememberCoroutineScope()

    if (showConfirmationDialog) {
        ThemedDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(stringResource(R.string.clear_history_dialog)) },
            confirm = {
                TextButton(onClick = {
                    scope.launch {
                        historyManager.clearHistory()
                    }.invokeOnCompletion {
                        showConfirmationDialog = false
                    }
                }) {
                    Text(
                        stringResource(R.string.clear),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            cancel = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            Text(stringResource(R.string.clear_history_dialog_content))
        }
    }
}
