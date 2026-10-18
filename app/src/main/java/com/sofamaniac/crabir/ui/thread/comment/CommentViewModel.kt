package com.sofamaniac.crabir.ui.thread.comment

import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.ui.votable.VotableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
open class CommentViewModel(
    @InjectedParam comment: CommentType.Comment,
    repository: CommentsRepository,
) :
    VotableViewModel<CommentType>(
        subreddit = comment.comment.subredditInfo.subredditPrefixed,
        fullname = comment.name,
        initialData = comment,
        repository = repository
    ) {

    override val votable: StateFlow<CommentType.Comment?> =
        super.votable.map { it as? CommentType.Comment }.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = comment
        )

    open fun collapse(collapsed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = repository.get(fullname).first()
            if (comment is CommentType.Comment) {
                val updated = comment.copy(comment = comment.comment.copy(collapsed = collapsed))
                repository.update(updated)
            }
        }
    }
}
