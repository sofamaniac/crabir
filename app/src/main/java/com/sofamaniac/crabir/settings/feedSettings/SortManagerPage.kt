package com.sofamaniac.crabir.settings.feedSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
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
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.Menu
import com.sofamaniac.crabir.settings.views.ViewSettings
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import kotlinx.coroutines.launch

@Composable
fun SortManagerPage() {

    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.viewSettingDataStore }
    val viewSettings by settingsDataStore.data.collectAsState(initial = ViewSettings())
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    val views = viewSettings.rememberedViews

    fun updateView(slug: String, view: CommunityViewEntity) {
        scope.launch {
            settingsDataStore.updateData {
                it.copy(
                    rememberedViews = it.rememberedViews + (slug to view)
                )
            }
        }
    }

    fun deleteView(slug: String) {
        val oldValue = viewSettings.rememberedViews[slug]!!
        scope.launch {
            settingsDataStore.updateData {
                it.copy(
                    rememberedViews = it.rememberedViews + (slug to oldValue.copy(
                        sort = null,
                        timeframe = null
                    ))
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.manage_sorts)) }, navigationIcon = {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(items = views.filter { entry -> entry.value.sort != null }
                .toList(), key = { it.first }) { it ->
                val slug = it.first
                val entity = it.second
                ViewTile(
                    entity.displayName, entity, updateView = {
                        updateView(slug, it)
                    },
                    deleteView = { deleteView(slug) }
                )
            }
        }
    }
}

@Composable
private fun ViewTile(
    community: String,
    entity: CommunityViewEntity,
    updateView: (CommunityViewEntity) -> Unit,
    deleteView: () -> Unit,
) {
    var showEditDialog by remember { mutableStateOf(false) }
    ListItem(
        onClick = { showEditDialog = true },
        trailingContent = {
            IconButton(onClick = { deleteView() }) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        },
        supportingContent = {
            Row() {
                if (entity.sort != null) {
                    Text(stringResource(entity.sort.representation))
                }
                VerticalDivider()
                if (entity.timeframe != null) {
                    Text(stringResource(entity.timeframe.representation))
                }
            }
        }
    ) {
        Text(community)
    }
    if (showEditDialog) {
        EditViewDialog(
            community,
            entity,
            onConfirm = updateView,
            onDismissRequest = { showEditDialog = false }
        )
    }
}

@Composable
internal fun EditViewDialog(
    community: String,
    entity: CommunityViewEntity,
    onConfirm: (CommunityViewEntity) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var sort: Sort? by remember { mutableStateOf(entity.sort) }
    var timeframe: Timeframe? by remember { mutableStateOf(entity.timeframe) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(entity.copy(sort = sort, timeframe = timeframe))
                onDismissRequest()
            }) { Text(stringResource(R.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(stringResource(R.string.cancel)) }
        },
        title = { Text(stringResource(R.string.sort_edit_title, community)) },
        text = {
            Column() {
                ListSelector(
                    options = Sort.entries.toList(),
                    selectedOption = sort,
                    optionLabel = {
                        stringResource(it.representation)
                    },
                    onOptionSelected = {
                        sort = it
                    },
                    label = { Text(stringResource(R.string.sort)) }
                )
                if (sort?.isTimeframe == true) {
                    ListSelector(
                        options = Timeframe.entries.toList(),
                        selectedOption = timeframe,
                        optionLabel = {
                            stringResource(it.representation)
                        },
                        onOptionSelected = {
                            timeframe = it
                        },
                        label = { Text(stringResource(R.string.timeframe)) }
                    )
                }
            }
        }
    )
}

@Composable
private fun <T> ListSelector(
    options: List<T>,
    selectedOption: T?,
    optionLabel: @Composable (T) -> String,
    label: @Composable () -> Unit,
    onOptionSelected: (T?) -> Unit,
) {
    val options = options + null
    ListItem(
        content = {
            Menu(
                options,
                selectedOption,
                enabled = true,
                onOptionSelected = onOptionSelected,
                label = label,
                optionLabel = {
                    if (it == null) {
                        stringResource(R.string.default_option)
                    } else {
                        optionLabel(it)
                    }
                }
            )
        }
    )
}
