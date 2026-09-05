package com.sofamaniac.crabir.settings.views

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
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.Menu
import kotlinx.coroutines.launch

@Composable
fun ViewManagerPage() {
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
                    rememberedViews = it.rememberedViews + (
                            slug to oldValue.copy(
                                view = null,
                                columns = null
                            )
                            )
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.manage_views)) }, navigationIcon = {
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
            items(
                items = views.filter { entry -> entry.value.view != null || entry.value.columns != null }
                    .toList(),
                key = { it.first }
            ) { view ->
                val slug = view.first
                val entity = view.second
                ViewTile(
                    entity.displayName,
                    entity,
                    updateView = { newView ->
                        updateView(slug, newView)
                    },
                    deleteView = { deleteView(slug) }
                )
            }
        }
    }
}

@Composable
internal fun ViewTile(
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
            Row {
                if (entity.view != null) {
                    Text(stringResource(entity.view.toStringResource()))
                }
                VerticalDivider()
                if (entity.columns != null) {
                    Text("${entity.columns} columns")
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
    var view: Views? by remember { mutableStateOf(entity.view) }
    var columns: Int? by remember { mutableStateOf(entity.columns) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(entity.copy(columns = columns, view = view))
                onDismissRequest()
            }) { Text(stringResource(R.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(stringResource(R.string.cancel)) }
        },
        title = { Text("Edit view for $community") },
        text = {
            Column {
                ListSelector(
                    options = Views.entries.toList(),
                    selectedOption = view,
                    optionLabel = {
                        stringResource(it.toStringResource())
                    },
                    onOptionSelected = {
                        view = it
                    },
                    label = { Text(stringResource(R.string.view)) }
                )
                ListSelector(
                    options = (1..3).toList(),
                    selectedOption = columns,
                    optionLabel = {
                        it.toString()
                    },
                    onOptionSelected = {
                        columns = it
                    },
                    label = { Text(stringResource(R.string.columns)) }
                )
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
