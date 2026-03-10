package com.sofamaniac.reboost.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.LicensesRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ThemeRoute
import com.sofamaniac.reboost.ViewsSettingRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
    val navController = LocalNavController.current!!
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ListItem(
                headlineContent = { Text("Theme") },
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = "Theme") },
                modifier = Modifier.clickable {
                    navController.navigate(ThemeRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Views") },
                leadingContent = { Icon(Icons.Default.ViewComfy, contentDescription = "Views") },
                modifier = Modifier.clickable {
                    navController.navigate(ViewsSettingRoute)
                }
            )
            ListItem(
                headlineContent = { Text("Licenses") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = "Licenses") },
                modifier = Modifier.clickable {
                    navController.navigate(LicensesRoute)
                }
            )
        }
    }
}