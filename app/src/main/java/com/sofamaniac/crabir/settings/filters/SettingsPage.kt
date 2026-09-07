package com.sofamaniac.crabir.settings.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled._18UpRating
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.ThemedDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersSettingsPage() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.filtersDataStore }
    val settings by settingsDataStore.data.collectAsState(FiltersSettings())
    val scope = rememberCoroutineScope()
    fun update(transform: (FiltersSettings) -> FiltersSettings) {
        scope.launch {
            settingsDataStore.updateData(transform)
        }

    }

    var editFilters: ((List<String>) -> Unit)? by remember { mutableStateOf(null) }
    var filtersToEdit: (List<String>)? by remember { mutableStateOf(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.filter_settings_title)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController?.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                SettingHeader(stringResource(R.string.nsfw_content))
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.showNSFW)) },
                    leadingContent = { Icon(Icons.Default._18UpRating, contentDescription = null) },
                    checked = settings.showNSFW,
                    onCheckedChange = { target -> update { it.copy(showNSFW = target) } }
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.showNSFWMedia)) },
                    leadingContent = { Icon(Icons.Default._18UpRating, contentDescription = null) },
                    checked = settings.showNSFWMedia,
                    onCheckedChange = { target -> update { it.copy(showNSFWMedia = target) } }
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.blurNSFW)) },
                    leadingContent = { Icon(Icons.Default._18UpRating, contentDescription = null) },
                    checked = settings.blurNSFW,
                    onCheckedChange = { target -> update { it.copy(blurNSFW = target) } }
                )
            }
            item {
                SettingHeader(stringResource(R.string.filters))
            }
            item {
                ListItem(
                    content = { Text(stringResource(R.string.edit_title_filters)) },
                    modifier = Modifier.clickable {
                        filtersToEdit = settings.titleFilters
                        editFilters =
                            { newFilters -> update { settings.copy(titleFilters = newFilters) } }
                    }
                )
            }
            item {
                ListItem(
                    content = { Text(stringResource(R.string.edit_author_filters)) },
                    modifier = Modifier.clickable {
                        filtersToEdit = settings.authorFilters
                        editFilters =
                            { newFilters -> update { settings.copy(authorFilters = newFilters) } }
                    }
                )
            }
            item {
                ListItem(
                    content = { Text(stringResource(R.string.edit_subreddit_filters)) },
                    modifier = Modifier.clickable {
                        filtersToEdit = settings.subredditFilters
                        editFilters =
                            { newFilters -> update { settings.copy(subredditFilters = newFilters) } }
                    }
                )
            }
            item {
                ListItem(
                    content = { Text(stringResource(R.string.edit_domains_filters)) },
                    modifier = Modifier.clickable {
                        filtersToEdit = settings.domainFilters
                        editFilters =
                            { newFilters -> update { settings.copy(domainFilters = newFilters) } }
                    }
                )
            }
            item {
                ListItem(
                    content = { Text(stringResource(R.string.edit_flair_filters)) },
                    modifier = Modifier.clickable {
                        filtersToEdit = settings.flairFilters
                        editFilters =
                            { newFilters -> update { settings.copy(flairFilters = newFilters) } }
                    }
                )
            }
        }
    }
    if (filtersToEdit != null) {
        FilterEditor(
            onDismissRequest = {
                filtersToEdit = null
                editFilters = null
            },
            filters = filtersToEdit!!,
            onChange = editFilters!!
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterEditor(
    onDismissRequest: () -> Unit,
    filters: List<String>,
    onChange: (List<String>) -> Unit,
) {
    var filters by remember { mutableStateOf(filters) }
    ThemedDialog(onDismissRequest) {
        LazyColumn {
            items(filters.size) { index ->
                ListItem(
                    content = {
                        TextField(
                            //enabled = editingIndex == index,
                            singleLine = true,
                            value = filters[index],
                            onValueChange = { newVal ->
                                filters = filters.mapIndexed { i, string ->
                                    if (i == index) {
                                        newVal
                                    } else {
                                        string
                                    }
                                }
                            }
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            filters = filters.filterIndexed { i, _ -> i != index }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
            item {
                ListItem(
                    content = {
                        IconButton(onClick = {
                            filters = filters + ""
                        }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(onClick = {
                onChange(filters)
                onDismissRequest()
            }) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Preview
@Composable
private fun TestEditor() {
    FilterEditor(onDismissRequest = {}, filters = listOf("Option 1", "Option 2", "Option 3")) { }
}

@Preview
@Composable
private fun PreviewSettingsPage() {
    FiltersSettingsPage()
}
