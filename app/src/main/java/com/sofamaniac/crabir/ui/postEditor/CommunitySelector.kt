package com.sofamaniac.crabir.ui.postEditor

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.data.remote.api.MissingCommunity
import com.sofamaniac.crabir.ui.subredditList.Tile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommunitySelector(
    viewModel: PostCreatorViewModel,
    modifier: Modifier = Modifier,
) {
    val fullscreenManager = LocalFullscreenHandler.current!!
    var showRules by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                fullscreenManager.push(
                    { CommunitySearch(viewModel) },
                )
            }
            .semantics {
                if (viewModel.error is MissingCommunity) {
                    error("Missing community")
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
            Text("Community")
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Search"
                )
            }
        } else {
            Tile(viewModel.community!!)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {
                viewModel.getRules()
                showRules = true
            }) { Text("RULES") }
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
                        ListItem(headlineContent = { Text(rule.shortName) })
                    }
                    items(viewModel.rules.siteRules.size) {
                        val rule = viewModel.rules.siteRules[it]
                        ListItem(headlineContent = { Text(rule) })
                    }
                }
                TextButton(onClick = { showRules = false }) {
                    Text("Close")
                }
            }
        }
    }
}