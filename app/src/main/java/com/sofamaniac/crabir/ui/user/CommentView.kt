package com.sofamaniac.crabir.ui.user

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.thread.CommentViewModelInterface
import com.sofamaniac.crabir.ui.thread.OpenedComment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun CommentView(
    thing: CommentData,
) {
    val navController = LocalNavController.current!!
    ThemedCard(
        modifier = Modifier.clickable {
            navController.navigate(
                PostRoute(
                    thing.permalink,
                    comment = thing.id,
                )
            )
        }
    ) {
        OpenedComment(
            thing,
            viewModel = hiltViewModel<CommentViewModel, CommentViewModel.Factory> { factory ->
                factory.create(thing)
            },
            enableAnimation = false,
        )
    }
}

@HiltViewModel(assistedFactory = CommentViewModel.Factory::class)
class CommentViewModel @AssistedInject constructor(
    @Assisted val comment: CommentData,
    private val commentsRepository: CommentsRepository,
) : CommentViewModelInterface, ViewModel() {
    override val openComment: StateFlow<Fullname?> = MutableStateFlow(comment.name)

    override val markdown = parseMarkdownFlow(comment.body.markdown).stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = State.Loading()
    )

    override fun submitComment(
        parent: Fullname,
        body: String
    ) {
        TODO("Not yet implemented")
    }

    override fun getMarkdownState(name: Fullname): StateFlow<State> = markdown

    override val likes: Flow<Boolean?> = flowOf(comment.relationship.liked)
    override val saved: Flow<Boolean> = flowOf(comment.relationship.saved)
    override val rules: StateFlow<Rules>
        get() = TODO("Not yet implemented")


    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsRepository.upvote(name)
        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsRepository.downvote(name)
        }
    }

    override fun save(name: Fullname, target: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                commentsRepository.unsave(name)
            } else {
                commentsRepository.save(name)
            }
        }
    }

    override fun fetchRules() {
        TODO("Not yet implemented")
    }

    override fun report(reason: String) {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    interface Factory {
        fun create(comment: CommentData): CommentViewModel
    }

}