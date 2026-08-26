package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalFiltersSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.settings.filters.filtersDataStore
import com.sofamaniac.crabir.ui.ThemedSwitch
import kotlinx.coroutines.launch

@Composable
internal fun NSFWTile() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val filtersDataStore = remember { context.filtersDataStore }
    val showNSFW = LocalFiltersSettings.current.showNSFW

    fun toggle() {
        coroutineScope.launch {
            filtersDataStore.updateData {
                it.copy(showNSFW = !it.showNSFW)
            }
        }
    }
    NavigationDrawerItem(
        selected = false,
        icon = {
            Icon(
                painter = painterResource(R.drawable.eighteen_rating),
                contentDescription = null
            )
        },
        label = {
            Text(stringResource(R.string.showNSFW))
        },
        badge = {
            ThemedSwitch(
                checked = showNSFW,
                onCheckedChange = { toggle() },
            )
        },
        onClick = { toggle() }
    )
}