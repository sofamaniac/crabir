package com.sofamaniac.crabir.settings.helper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> ListSelector(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit = {
        Spacer(modifier = Modifier.size(24.dp))
    },
    headlineContent: @Composable () -> Unit = {},
    enabled: Boolean = true,
    optionLabel: @Composable (T) -> String = { it.toString() },
) {
    ListItem(
        enabled = enabled,
        modifier = modifier,
        leadingContent = leadingContent,
        shapes = ListItemDefaults.shapes(shape = ShapeDefaults.Medium.copy(all = ZeroCornerSize)),
        //headlineContent = headlineContent,
        content = {
            Menu(
                options,
                selectedOption,
                onOptionSelected = onOptionSelected,
                label = headlineContent,
                optionLabel = optionLabel
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun <T> Menu(
    options: List<T>,
    selected: T,
    enabled: Boolean = true,
    onOptionSelected: (T) -> Unit,
    label: @Composable () -> Unit = {},
    optionLabel: @Composable (T) -> String = { it.toString() },
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(optionLabel(selected))
    val text = optionLabel(selected)
    LaunchedEffect(selected) {
        textFieldState.setTextAndPlaceCursorAtEnd(text)
    }

    ExposedDropdownMenuBox(expanded = expanded && enabled, onExpandedChange = { expanded = it }) {
        TextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            state = textFieldState,
            enabled = enabled,
            readOnly = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            label = { label() },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false },
            containerColor = MenuDefaults.groupStandardContainerColor,
            shape = MenuDefaults.standaloneGroupShape,
        ) {
            val optionCount = options.size
            options.forEachIndexed { index, option ->
                val label = optionLabel(option)
                DropdownMenuItem(
                    shapes = MenuDefaults.itemShape(index, optionCount),
                    text = { Text(label, style = MaterialTheme.typography.bodyLarge) },
                    selected = option == selected,
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(label)
                        onOptionSelected(option)
                        expanded = false
                    },
                    selectedLeadingIcon = {
                        Icon(
                            Icons.Filled.Check,
                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                            contentDescription = null,
                        )
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun TestSelector() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selected by remember { mutableStateOf(options[0]) }
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