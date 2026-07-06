package com.sofamaniac.crabir.ui.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.post.LinkViewModel
import com.sofamaniac.crabir.ui.post.buttons.OpenThreadButton
import com.sofamaniac.crabir.ui.post.dialog.ShareMenu
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.ScoreString
import com.sofamaniac.crabir.ui.votable.UpButton
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ColumnScope.FullscreenTopBar(
    enabled: Boolean,
    actions: @Composable () -> Unit = {}
) {
    val navController = LocalNavController.current
    AnimatedVisibility(
        visible = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .background(color = Color.Black.copy(alpha = 0.6f))
            .statusBarsPadding(),
        label = "decoration animation"
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            actions()
        }
    }
}

@Composable
fun ColumnScope.FullscreenBottomBar(
    post: PostData,
    enabled: Boolean,
    viewModel: LinkViewModel = koinViewModel(
        key = post.id
    ) { parametersOf(post) },
    title: @Composable () -> Unit = {}
) {
    val theme = LocalTheme.current
    val likes by viewModel.likes.collectAsState(post.relationship.liked)
    val saved by viewModel.saved.collectAsState(post.relationship.saved)
    val navController = LocalNavController.current
    val currentAccount = LocalRedditAccount.current
    var showShareMenu by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .background(color = Color.Black.copy(alpha = 0.6f))
            .navigationBarsPadding(),
        label = "decoration animation"
    ) {

        Column(verticalArrangement = Arrangement.Bottom) {
            title()

            Text(
                post.title,
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                modifier = Modifier.padding(start = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UpButton(likes, onClick = { viewModel.upvote(post.name) })
                    ScoreString(post.score.score, likes)
                    DownButton(likes, onClick = { viewModel.downvote(post.name) })
                }
                SavedButton(saved, onClick = { viewModel.save(post.name, !saved) })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OpenThreadButton {
                        viewModel.markPost(post, currentAccount.id)
                        navController?.navigate(PostRoute(post.permalink))
                    }
                    Text("${post.numComments}", color = theme.secondaryText)
                }
                IconButton(onClick = { showShareMenu = true }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "share",
                        tint = theme.secondaryText
                    )
                }
            }
        }
    }
    if (showShareMenu) {
        ShareMenu(post) { showShareMenu = false }
    }
}