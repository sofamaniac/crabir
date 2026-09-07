package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.ui.ListItem

fun LazyListScope.goToMenu(expanded: Boolean = false, onClick: () -> Unit) {
    item {
        ListItem(
            onClick = onClick,
            colors = ListItemDefaults.colors()
                .copy(containerColor = DrawerDefaults.modalContainerColor),
            leadingContent = {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
            }, content = {
                Text(
                    stringResource(R.string.go_to),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            trailingContent = {
                if (expanded) {
                    Icon(Icons.Default.ArrowDropUp, contentDescription = null)
                } else {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
    }
    if (expanded) {
        item { RandomCommunity(stringResource(R.string.random_community), false) }
        item { RandomCommunity(stringResource(R.string.random_nsfw), true) }
    }
}
