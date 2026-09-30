package com.sofamaniac.crabir.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FlowRowWithSeparator(
    separator: @Composable FlowRowScope.() -> Unit,
    modifier: Modifier = Modifier.Companion,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxItemInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    content: List<@Composable FlowRowScope.() -> Unit>,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemInEachRow,
        maxLines = maxLines
    ) {
        for (index in 0..<(content.size - 1)) {
            content[index]()
            separator()
        }
        content.last()()
    }
}
