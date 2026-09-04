package com.sofamaniac.crabir.settings.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.ThemedCard
import kotlinx.coroutines.launch

@Composable
fun ApiSettingsPage() {
    val context = LocalContext.current
    val datastore = remember(context) { context.apiSettingsDataStore }
    val apiSettings by datastore.data.collectAsState(
        initial = ApiSettings(),
    )
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    fun update(transform: (ApiSettings) -> ApiSettings) {
        scope.launch {
            datastore.updateData(transform)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.api_settings_title)) },
                navigationIcon = {
                    BackButton {
                        navController?.popBackStack()
                    }
                })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                val fieldState =
                    rememberTextFieldState(initialText = apiSettings.redditClientId ?: "")
                ThemedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            stringResource(R.string.client_id),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            stringResource(R.string.client_id_support),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextField(state = fieldState)
                        val enabled =
                            fieldState.text != apiSettings.redditClientId && fieldState.text.isNotBlank()
                        TextButton(
                            enabled = enabled,
                            onClick = {
                                update { it.copy(redditClientId = fieldState.text.toString()) }
                            }) {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
            }
        }
    }
}
