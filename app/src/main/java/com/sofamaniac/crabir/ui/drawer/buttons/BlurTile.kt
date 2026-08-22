package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.sofamaniac.crabir.LocalFiltersSettings
import com.sofamaniac.crabir.settings.filters.filtersDataStore
import com.sofamaniac.crabir.ui.ThemedSwitch
import kotlinx.coroutines.launch

@Composable
internal fun BlurTile() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val filtersDataStore = remember { context.filtersDataStore }
    val blur = LocalFiltersSettings.current.blurNSFW

    fun toggle() {
        coroutineScope.launch {
            filtersDataStore.updateData {
                it.copy(blurNSFW = !it.blurNSFW)
            }
        }
    }
    NavigationDrawerItem(
        selected = false,
        icon = { Icon(Icons.Default.BlurOn, contentDescription = "Blur NSFW") },
        label = {
            Text("Blur NSFW")
        },
        badge = {
            ThemedSwitch(
                checked = blur,
                onCheckedChange = { toggle() },
            )
        },
        onClick = { toggle() }
    )
}