/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.toRoute
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.PostRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadView(
    modifier: Modifier = Modifier,
    permalink: String? = null,
    dismiss: () -> Unit = {},
) {
    val link = permalink
        ?: LocalNavController.current?.currentBackStackEntry?.toRoute<PostRoute>()?.postPermalink

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val viewModel: ThreadViewModel =
        hiltViewModel<ThreadViewModel, ThreadViewModel.Factory>(key = link) { factory ->
            factory.create(link!!)
        }

    Scaffold(
        topBar = { TopBar(viewModel, scrollBehavior, dismiss) },
        //bottomBar = { TabBar(selected) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        CommentListRoot(
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}