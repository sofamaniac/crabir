package com.sofamaniac.crabir.ui.votable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.Kind
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.ui.ThemedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportMenu(
    name: Fullname,
    viewModel: VotableInteraction,
    kind: Kind = Kind.All,
    onDismissRequest: () -> Unit,
) {
    val rulesFull by viewModel.rules.collectAsState()
    val rules = rulesFull.filter(kind)
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(rules.siteRules.firstOrNull()) }
    Dialog(onDismissRequest) {
        ThemedCard(modifier = Modifier.padding(16.dp)) {
            Column {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    for (rule in rules.rules) {
                        ListItem(
                            modifier = Modifier.selectable(
                                selected = selectedOption == rule.violationReason,
                                onClick = { setSelectedOption(rule.violationReason) },
                                role = Role.RadioButton
                            ),
                            leadingContent = {
                                RadioButton(
                                    selected = selectedOption == rule.violationReason,
                                    onClick = null
                                )
                            },
                            content = { Text(rule.shortName) }
                        )
                    }
                    for (rule in rules.siteRules) {
                        ListItem(
                            modifier = Modifier.selectable(
                                selected = selectedOption == rule,
                                onClick = { setSelectedOption(rule) },
                                role = Role.RadioButton
                            ),
                            leadingContent = {
                                RadioButton(
                                    selected = selectedOption == rule,
                                    onClick = null
                                )
                            },
                            content = { Text(rule) }
                        )
                    }
                }

                Row {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        enabled = selectedOption != null,
                        onClick = {
                            viewModel.report(name, selectedOption!!)
                            onDismissRequest()
                        }) {
                        Text(stringResource(R.string.report))
                    }
                }
            }
        }
    }
}