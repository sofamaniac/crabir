package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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

                else -> {
                    Log.d("TopBar", "No sort menu")
                }
            }
        }
    )
}
