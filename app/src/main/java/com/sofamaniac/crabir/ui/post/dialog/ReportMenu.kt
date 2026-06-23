package com.sofamaniac.crabir.ui.post.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.ui.post.LinkInteraction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportMenu(viewModel: LinkInteraction, onDismissRequest: () -> Unit) {
    val rules by viewModel.rules.collectAsState()
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(rules.siteRules.firstOrNull()) }
    BasicAlertDialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxHeight(0.8f)
            ) {
                items(rules.rules.size) { index ->
                    ListItem(
                        modifier = Modifier.selectable(
                            selected = selectedOption == rules.rules[index].violationReason,
                            onClick = { setSelectedOption(rules.rules[index].violationReason) },
                            role = Role.RadioButton
                        ),
                        leadingContent = {
                            RadioButton(
                                selected = selectedOption == rules.rules[index].violationReason,
                                onClick = null
                            )
                        },
                        headlineContent = { Text(rules.rules[index].shortName) }
                    )
                }
                items(rules.siteRules.size) { index ->
                    ListItem(
                        modifier = Modifier.selectable(
                            selected = selectedOption == rules.siteRules[index],
                            onClick = { setSelectedOption(rules.siteRules[index]) },
                            role = Role.RadioButton
                        ),
                        leadingContent = {
                            RadioButton(
                                selected = selectedOption == rules.siteRules[index],
                                onClick = null
                            )
                        },
                        headlineContent = { Text(rules.siteRules[index]) }
                    )
                }
            }

            Row {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    if (selectedOption == null) return@TextButton
                    viewModel.report(selectedOption)
                    onDismissRequest()
                }) {
                    Text(stringResource(R.string.report))
                }
            }
        }
    }
}