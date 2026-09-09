package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.ThreadRepository
import com.sofamaniac.crabir.settings.comments.CommentsSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

sealed class UiState {
    object Loading : UiState()
    class Error(val e: Throwable) : UiState()
    object Success : UiState()
}

@KoinViewModel
class ThreadViewModel(
    private val repository: ThreadRepository,
    private val commentsRepository: CommentsRepository,
    accountsRepository: AccountsRepository,
    @InjectedParam val permalink: String,
    @InjectedParam val comment: String?,
    @InjectedParam val context: Int?,
    @InjectedParam val commentsSettings: CommentsSettings,
) : ViewModel() {

    var name: Fullname = repository.getPostId(permalink)
    private var _post = MutableStateFlow<PostData?>(null)
    val post: StateFlow<PostData?> = _post.asStateFlow()

    val accounts: Flow<List<RedditAccount>> = accountsRepository.accounts

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var initialLoad: Boolean = false
    var commentsLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val replyState = MutableStateFlow<Fullname?>(null)
    val reply: StateFlow<Fullname?> = replyState.asStateFlow()

    val listState = LazyListState()


    private val commentsNames = repository.comments

    @OptIn(ExperimentalCoroutinesApi::class)
    val comments = commentsNames.flatMapLatest { names ->
        commentsRepository.getMany(names).map { comments ->
            val table = comments.associateBy { it.name }
            val orderedList = names.mapNotNull { table[it] }
            buildList {
                var collapsedDepthThreshold: Int? = null
                for (comment in orderedList) {
                    if (collapsedDepthThreshold != null) {
                        if (comment.depth > collapsedDepthThreshold) continue
                        collapsedDepthThreshold = null
                    }
                    add(comment)
                    if (comment is CommentType.Comment && comment.comment.collapsed) {
                        collapsedDepthThreshold = comment.depth
                    }
                }
            }
        }.dropWhile { it.isEmpty() }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private var _openComment = MutableStateFlow<Fullname?>(null)

    /** Id of the comment of which the bottom bar is currently open */
    val openComment: StateFlow<Fullname?> = _openComment.asStateFlow()

    /** If [openComment] is equal to [name], close it. Otherwise, open it. */
    fun toggleComment(name: Fullname, target: Boolean? = null) {
        if (target == true) {
            _openComment.value = name
        } else if (target == false) {
            _openComment.value = null
        } else if (_openComment.value == name) {
            _openComment.value = null
        } else {
            _openComment.value = name
        }
    }

    fun closeComment(name: Fullname) {
        if (_openComment.value == name) {
            _openComment.value = null
        }
    }

    private val _sort = MutableStateFlow<Sort?>(null)
    val sort: StateFlow<Sort?> = _sort.asStateFlow()

    init {
        _sort.value =
            if (commentsSettings.useRecommendedSort) null else commentsSettings.preferredSort
        fetchComments()
    }

    private suspend fun getPost(): PostData? {
        val post = repository.getPost(name)
        if (post == null) {
            Log.e("ThreadViewModel", "getPost: Post not found in database ($name)")
        }
        return post
    }

    private suspend fun fetchAsync() {
        _uiState.update { UiState.Loading }
        repository.getComments(
            permalink,
            sort = _sort.value,
            comment = comment,
            context = context
        )
        // If post was not found set it here.
        _post.value = getPost() ?: _post.value
        _sort.value = _sort.value ?: _post.value?.suggestedSort
        _uiState.update { UiState.Success }
    }

    fun fetchComments() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAsync()
            if (commentsSettings.collapseAutoMod) {
                val automodComments =
                    comments.value.filter {
                        it is CommentType.Comment &&
                                it.comment.author.username == "AutoModerator"
                    }
                for (comment in automodComments) {
                    val comment = comment as CommentType.Comment
                    repository.updateComment(
                        name,
                        CommentType.Comment(comment = comment.comment.copy(collapsed = true))
                    )
                }
            }
            commentsLoaded.update { true }
        }
    }

    fun fetchMoreComments(more: CommentType.More) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMoreComments(more)
        }
    }

    fun setSort(sort: Sort) {
        _sort.value = sort
        refresh()
    }


    fun refresh() {
        repository.refresh()
        fetchComments()
    }

    fun replyTo(name: Fullname?) {
        replyState.value = name
    }

    fun submitComment(parent: Fullname, body: String, account: RedditAccount?) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.postComment(parent, body, account = account)
            if (!response.isSuccess) {
                return@launch
            }
            val result = response.getOrNull()?.json ?: return@launch
            // TODO display error if any
            val comment = result.data?.things?.firstOrNull() ?: return@launch
            val commentDTO = comment as Thing.Comment
            var commentData = CommentDataMapper.map(commentDTO.data)
            commentData =
                commentData.copy(relationship = commentData.relationship.copy(liked = true))
            val p = comments.value.find { it.name == parent }
            commentData = commentData.copy(depth = (p?.depth ?: -1) + 1)
            repository.insertReply(parent, CommentType.Comment(commentData))
            replyState.value = null
        }
    }
}
