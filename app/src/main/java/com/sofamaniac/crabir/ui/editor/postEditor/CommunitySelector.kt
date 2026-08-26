package com.sofamaniac.crabir.ui.editor.postEditor

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.MissingCommunity
import com.sofamaniac.crabir.ui.subredditList.Tile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommunitySelector(
    viewModel: CreatorViewModel,
    modifier: Modifier = Modifier,
    communitySearch: @Composable (onDismiss: () -> Unit) -> Unit,
) {
    var showRules by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var showSearch by remember { mutableStateOf(false) }
    val missingCommunityErrorMessage = stringResource(R.string.missing_community_error)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                showSearch = !showSearch
            }
            .semantics {
                if (viewModel.error is MissingCommunity) {
                    error(missingCommunityErrorMessage)
                }
            }
            .let {
                if (viewModel.error is MissingCommunity && viewModel.community == null) {
                    it.border(
                        1.dp,
                        MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small
                    )
                } else {
                    it
                }
            }
            .padding(all = 8.dp)
    ) {
        if (viewModel.community == null) {
            Text(stringResource(R.string.community_placeholder))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.search_icon_label)
                )
            }
        } else {
            Tile(viewModel.community!!)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {
                scope.launch {
                    viewModel.getRules()
                    showRules = true
                }
            }) { Text(stringResource(R.string.rules_button)) }
        }
    }
    if (showRules) {
        Dialog(onDismissRequest = { showRules = false }) {
            Card(
                modifier = Modifier
                    .heightIn(max = 800.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyColumn(state = rememberLazyListState()) {
                    items(viewModel.rules.rules.size) { index ->
                        val rule = viewModel.rules.rules[index]
                        ListItem(content = { Text(rule.shortName) })
                    }
                    items(viewModel.rules.siteRules.size) {
                        val rule = viewModel.rules.siteRules[it]
                        ListItem(content = { Text(rule) })
                    }
                }
                TextButton(onClick = { showRules = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        }
    }
    if (showSearch) {
        communitySearch { showSearch = false }
    }
}