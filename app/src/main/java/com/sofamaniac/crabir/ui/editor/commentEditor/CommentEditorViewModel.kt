package com.sofamaniac.crabir.ui.editor.commentEditor

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import com.sofamaniac.crabir.data.remote.reddit.MoreResponseOuter
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.commentSubmissionBody
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CommentEditorViewModel(
    @InjectedParam parentRaw: String,
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

    fun finalize(): String {
        return replyState.text.toString()
    }

    suspend fun submitComment(account: RedditAccount?): Result<MoreResponseOuter> {
        val body = commentSubmissionBody(parent, replyState.text.toString())
        val res = api.submitComment(body, account = account)
        return if (res.isSuccessful) {
            Result.success(res.body()!!)
        } else {
            Result.failure(Exception(res.errorBody()?.string()))
        }
    }
}