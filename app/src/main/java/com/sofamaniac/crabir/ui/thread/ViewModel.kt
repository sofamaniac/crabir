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
import com.sofamaniac.crabir.domain.repository.ThreadRepository
import com.sofamaniac.crabir.settings.comments.CommentsSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ThreadViewModel(
    private val repository: ThreadRepository,
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

    var initialLoad: Boolean = false
    var commentsLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val replyState = MutableStateFlow<Fullname?>(null)
    val reply: StateFlow<Fullname?> = replyState.asStateFlow()

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    val listState = LazyListState()

    val comments = repository.comments


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
        _isRefreshing.value = true
        repository.getComments(
            permalink,
            sort = _sort.value,
            comment = comment,
            context = context
        )
        Log.d("ThreadViewModel", "commentFlow: ${comments.value.count()}")
        // If post was not found set it here.
        _post.value = getPost() ?: _post.value
        _sort.value = _sort.value ?: _post.value?.suggestedSort
        _isRefreshing.value = false
    }

    fun fetchComments() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAsync()
            if (commentsSettings.collapseAutoMod) {
                for (comment in comments.value.filter { it is CommentType.Comment && it.comment.author.username == "AutoModerator" }) {
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