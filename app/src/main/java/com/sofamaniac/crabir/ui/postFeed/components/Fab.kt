package com.sofamaniac.crabir.ui.postFeed.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.postFeed.FeedViewModelInterface
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Fab(viewModel: FeedViewModelInterface<PostData>, toggleBottomSheet: () -> Unit) {
    val scope = rememberCoroutineScope()
    var expandFab by remember { mutableStateOf(false) }
    val showFab by remember {
        derivedStateOf {
            !viewModel.listState.canScrollBackward
                    || !viewModel.listState.canScrollForward
                    || viewModel.listState.lastScrolledBackward
        }
    }
    val theme = LocalTheme.current
    FloatingActionButtonMenu(
        expanded = expandFab,
        button = {
            ToggleFloatingActionButton(
                containerColor = { theme.highlight },
                modifier = Modifier
                    .semantics {
                        stateDescription =
                            if (expandFab) "Expanded" else "Collapsed"
                        contentDescription = "Toggle menu"
                    }
                    .animateFloatingActionButton(
                        visible = showFab || expandFab,
                        alignment = Alignment.BottomEnd
                    ),
                checked = expandFab,
                onCheckedChange = { expandFab = !expandFab },
            ) {

                val icon by remember {
                    derivedStateOf {
                        if (checkedProgress > 0.5f) Icons.Default.Close else Icons.Default.Add
                    }
                }
                val tint = contentColorFor(theme.highlight)
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.animateIcon(
                        { checkedProgress },
                        color = { tint }
                    ),
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = { scope.launch { viewModel.listState.scrollToItem(0) } },
            containerColor = theme.highlight,
            icon = {
                Icon(
                    Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = null
                )
            },
            text = { Text(stringResource(R.string.go_to_top)) }
        )
        FloatingActionButtonMenuItem(
            onClick = { toggleBottomSheet() },
            containerColor = theme.highlight,
            icon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null
                )
            },
            text = { Text(stringResource(R.string.create_post)) }
        )
    }
}
