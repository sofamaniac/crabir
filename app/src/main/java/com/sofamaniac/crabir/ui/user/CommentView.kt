package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.thread.CommentViewModel
import com.sofamaniac.crabir.ui.thread.OpenedComment
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CommentView(
    thing: CommentType.Comment,
) {
    val navController = LocalNavController.current
    val comment = thing.comment
    Log.d("CommentView", "comment ${thing.body}")
    ThemedCard(
        roundedCorners = false,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.clickable {
            navController?.navigate(
                PostRoute(
                    comment.permalink,
                    comment = thing.id,
                )
            )
        }
    ) {
        OpenedComment(
            viewModel = koinViewModel<CommentViewModel> { parametersOf(thing) },
            enableAnimation = false,
            opened = true,
            toggleComment = {},
            startReply = {},
        )
    }
}