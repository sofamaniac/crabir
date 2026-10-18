package com.sofamaniac.crabir.settings.helper

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.settings.theme.ConfigureCrabirTheme
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.ListItemSpacer
import com.sofamaniac.crabir.ui.components.ThemedDialog

@Composable
fun <T> ListSelector(
    options: List<T>,
    selectedOption: T,
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit = {
        ListItemSpacer()
    },
    headlineContent: @Composable () -> Unit = {},
    enabled: Boolean = true,
    optionLabel: @Composable (T) -> String = { it.toString() },
    onOptionSelected: (T) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    ListItem(
        enabled = enabled,
        modifier = modifier,
        leadingContent = leadingContent,
        shapes = ListItemDefaults.shapes(shape = ShapeDefaults.Medium.copy(all = ZeroCornerSize)),
        //headlineContent = headlineContent,
        onClick = { showDialog = true },
        content = {
            headlineContent()
        },
        supportingContent = {
            Text(optionLabel(selectedOption))
        }
    )
    if (showDialog) {
        MenuDialog(
            options,
            selectedOption,
            onOptionSelected = onOptionSelected,
            dismissRequest = { showDialog = false },
            optionLabel = optionLabel
        )
    }
}

@Composable
fun <T> ListSelectorOption(
    options: List<T>,
    selectedOption: T?,
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit = {
        ListItemSpacer()
    },
    headlineContent: @Composable () -> Unit = {},
    enabled: Boolean = true,
    optionLabel: @Composable (T) -> String = { it.toString() },
    onOptionSelected: (T?) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val innerOptionLabel = @Composable { option: T? ->
        if (option == null) {
            stringResource(R.string.default_option)
        } else {
            optionLabel(option)
        }
    }
    ListItem(
        enabled = enabled,
        modifier = modifier,
        leadingContent = leadingContent,
        shapes = ListItemDefaults.shapes(shape = ShapeDefaults.Medium.copy(all = ZeroCornerSize)),
        //headlineContent = headlineContent,
        onClick = { showDialog = true },
        content = {
            headlineContent()
        },
        supportingContent = {
            Text(innerOptionLabel(selectedOption))
        }
    )
    if (showDialog) {
        MenuDialog(
            options,
            selectedOption,
            onOptionSelected = onOptionSelected,
            dismissRequest = { showDialog = false },
            optionLabel = innerOptionLabel
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun <T> MenuDialog(
    options: List<T>,
    selected: T,
    onOptionSelected: (T) -> Unit,
    dismissRequest: () -> Unit,
    optionLabel: @Composable (T) -> String = { it.toString() },
) {
    var selectedOption by remember { mutableStateOf(selected) }
    ThemedDialog(
        onDismissRequest = dismissRequest,
        cancel = {
            TextButton(onClick = dismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirm = {
            TextButton(onClick = {
                dismissRequest()
                onOptionSelected(selectedOption)
            }) {
                Text(stringResource(R.string.confirm))
            }
        }
    ) {
        Column(modifier = Modifier.selectableGroup()) {
            for (option in options) {
                val selected = option == selectedOption
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            onClick = {
                                selectedOption = option
                            },
                            role = Role.RadioButton
                        )
                ) {
                    RadioButton(selected = selected, onClick = null)
                    Text(optionLabel(option), modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@SuppressLint("HardcodedComposeText")
@Preview
@Composable
internal fun TestSelector() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selected by remember { mutableStateOf(options[0]) }
    ConfigureCrabirTheme {
        Scaffold { padding ->
            Column(modifier = Modifier.padding(padding)) {
                ListSelector(
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = null
                        )
                    },
                    headlineContent = { Text("HEADLINE CONTENT") },
                    options = options,
                    selectedOption = selected,
                    onOptionSelected = { selected = it },
                    optionLabel = { it }
                )
            }
        }
    }
}
