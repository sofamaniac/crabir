package com.sofamaniac.crabir.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.domain.repository.profile.ProfileSort
import com.sofamaniac.crabir.settings.theme.rememberTopAppBarColors
import com.sofamaniac.crabir.ui.components.SortMenu

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    user: String,
    userInfo: UserDTO?,
    modifier: Modifier = Modifier,
    viewModel: ProfileFeedViewModel<*>?,
    openDrawer: () -> Unit,
) {
    TwoRowsTopAppBar(
        scrollBehavior = scrollBehavior,
        colors = rememberTopAppBarColors(),
        title = { expanded ->
            if (expanded && userInfo != null) {
                val iconUrl =
                    if (userInfo.prefShowSnoovatar && !userInfo.snoovatarImg.isNullOrBlank()) {
                        userInfo.snoovatarImg
                    } else {
                        userInfo.iconImg
                    }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier.align(Alignment.CenterHorizontally)) {
                        if (userInfo.bannerImg != null) {
                            AsyncImage(
                                model = userInfo.bannerImg,
                                contentDescription = "$user banner",
                                modifier = Modifier
                                    .fillMaxWidth()
                                //.offset(y = (-32).dp)
                            )
                        }
                        AsyncImage(
                            model = iconUrl,
                            contentDescription = "$user icon",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(
                                    CircleShape
                                )
                        )
                    }
                    Text(user, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(user)
                }
            }
        }, navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    Icons.Default.Menu, stringResource(R.string.open_drawer)
                )
            }
        }, actions = {
            when (viewModel) {
                is SortProfileTab -> {
                    SortMenu<ProfileSort> { sort, timeframe ->
                        viewModel.updateSort(sort, timeframe)
                    }
                }

                is SavedViewModel -> {
                    val filter by viewModel.currentFilter.collectAsState()
                    SavedFilterMenu(filter) { filter ->
                        viewModel.updateFilter(filter)
                    }
                }

                else -> {}
            }
        }
    )
}

@Composable
fun SavedFilterMenu(selected: SavedFilter, update: (SavedFilter) -> Unit) {
    var showDropdown by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { showDropdown = true }) {
            Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
        }
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier.selectableGroup()
        ) {
            SavedFilter.entries.forEach { filter ->
                val isSelected = filter == selected
                DropdownMenuItem(
                    text = { Text(filter.toStringResource()) },
                    onClick = {
                        update(filter)
                        showDropdown = false
                    },
                    modifier = Modifier.selectable(
                        selected = isSelected,
                        onClick = {
                            update(filter)
                            showDropdown = false
                        },
                        role = Role.RadioButton
                    ),
                    trailingIcon = {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = "Selected")
                        }
                    }
                )
            }
        }
    }
}
