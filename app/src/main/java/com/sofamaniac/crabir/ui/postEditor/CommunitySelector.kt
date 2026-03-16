package com.sofamaniac.crabir.ui.postEditor

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
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
            TextButton(onClick = {}) { Text("RULES") }
        }
    }
}