package com.sofamaniac.crabir.settings.views

import android.util.Log
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
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.Menu

@Composable
fun ViewManagerPage() {

    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.viewSettingDataStore }
    val viewSettings by settingsDataStore.data.collectAsState(initial = ViewSettings())
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    val views = viewSettings.rememberedViews
    Log.d("ViewManagerPage", "views: $views")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manage views") }, navigationIcon = {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(items = views.values.toList(), key = { it.name }) {
                ViewTile(it.displayName, it.view, it.columns)
            }
        }
    }
}

@Composable
fun ViewTile(community: String, view: Views?, columns: Int?) {
    var showEditDialog by remember { mutableStateOf(false) }
    ListItem(
        onClick = { showEditDialog = true },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        },
        supportingContent = {
            Row() {
                if (view != null) {
                    Text(stringResource(view.toStringResource()))
                }
                VerticalDivider()
                if (columns != null) {
                    Text("$columns columns")
                }
            }
        }
    ) {
        Text(community)
    }
    if (showEditDialog) {
        EditViewDialog(community, view, columns, onDismissRequest = { showEditDialog = false })
    }
}

@Composable
fun EditViewDialog(
    community: String,
    view: Views? = null,
    columns: Int? = null,
    onDismissRequest: () -> Unit,
) {
    var view: Views? by remember { mutableStateOf(view) }
    var columns: Int? by remember { mutableStateOf(columns) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {}) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("Cancel") }
        },
        title = { Text("Edit view for $community") },
        text = {
            Column() {
                ListSelector(
                    options = Views.entries.toList(),
                    selectedOption = view,
                    optionLabel = {
                        stringResource(it.toStringResource())
                    },
                    onOptionSelected = {
                        view = it
                    },
                    label = { Text("View") }
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
                    label = { Text("Columns") }
                )
            }
        }
    )
}

@Composable
internal fun <T> ListSelector(
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
                onOptionSelected,
                label = label,
                optionLabel = {
                    if (it == null) {
                        "Default"
                    } else {
                        optionLabel(it)
                    }
                }
            )
        }
    )
}