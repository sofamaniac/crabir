package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sofamaniac.crabir.data.remote.dto.SortInterface
import com.sofamaniac.crabir.data.remote.dto.Timeframe

@Preview
@Composable
inline fun <reified Sort> SortMenu(crossinline onSelect: (Sort, Timeframe?) -> Unit = { _, _ -> })
        where Sort : Enum<Sort>, Sort : SortInterface {
    var sortExpanded by remember { mutableStateOf(false) }
    var timeframeExpanded by remember { mutableStateOf(false) }
    var chosenSort by remember { mutableStateOf<Sort?>(null) }
    val entries = enumValues<Sort>()
    Box {
        IconButton(onClick = { sortExpanded = true }) {
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
        }
        DropdownMenu(
            expanded = sortExpanded,
            onDismissRequest = { sortExpanded = false }
        ) {
            entries.forEach { sort ->
                if (sort.isTimeframe) {
                    DropdownMenuItem(
                        text = { Text(stringResource(sort.representation)) },
                        onClick = {
                            timeframeExpanded = true
                            chosenSort = sort
                        },
                        trailingIcon = {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowRight,
                                contentDescription = "Select"
                            )
                        }
                    )
                } else {
                    DropdownMenuItem(
                        text = { Text(stringResource(sort.representation)) },
                        onClick = {
                            onSelect(sort, null)
                            sortExpanded = false
                        }
                    )
                }
            }
        }
        TimeframeMenu(expanded = timeframeExpanded, onDismiss = { timeframeExpanded = false }) {
            onSelect(chosenSort!!, it)
            sortExpanded = false
            timeframeExpanded = false
        }
    }
}

@Composable
fun TimeframeMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSortChange: (Timeframe?) -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        Timeframe.entries.forEach {
            DropdownMenuItem(text = { Text(it.toString()) }, onClick = {
                onSortChange(it)
            })
        }
    }
}
