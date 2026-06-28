package com.sofamaniac.crabir.ui.editor.commentEditor

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.postCommentBody
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CommentEditorViewModel.Factory::class)
class CommentEditorViewModel @AssistedInject constructor(
    @Assisted parentRaw: String,
    commentRepository: CommentsRepository,
    postRepository: LinksRepository,
    private val api: RedditAPIService,
) : ViewModel() {

    val parent = Fullname(parentRaw)

    val parentData: Flow<VotableData?> = if (parent.name.startsWith("t1")) {
        commentRepository.get(parent)
    } else {
        postRepository.get(parent)
    }

    val replyState = TextFieldState()

    fun submitComment() {
        viewModelScope.launch {
            val body = postCommentBody(parent, replyState.text.toString())
            api.postComment(body)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(parentRaw: String): CommentEditorViewModel
    }
}