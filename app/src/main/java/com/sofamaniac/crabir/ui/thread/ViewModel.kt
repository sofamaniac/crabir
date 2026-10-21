package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.accounts.AccountsStorage
import com.sofamaniac.crabir.domain.repository.thread.ThreadRepository
import com.sofamaniac.crabir.settings.comments.CommentsSettings
import com.sofamaniac.crabir.settings.history.HistoryManager
import com.sofamaniac.crabir.ui.votable.CommentEvent
import com.sofamaniac.crabir.ui.votable.VotableEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

sealed interface LoadingState {
    object Loading : LoadingState
    class Error(val e: Throwable) : LoadingState
    object Success : LoadingState
}

sealed interface Dialog {
    class Reply(val to: Fullname) : Dialog
    class Share(val name: Fullname) : Dialog
    class Copy(val name: Fullname) : Dialog
    class Report(val name: Fullname) : Dialog
    class User(val username: String) : Dialog

    class Edit(val name: Fullname) : Dialog
}


@KoinViewModel
class ThreadViewModel(
    private val repository: ThreadRepository,
    private val commentsRepository: CommentsRepository,
    accountsStorage: AccountsStorage,
    private val historyManager: HistoryManager,
    @InjectedParam val permalink: String,
    @InjectedParam val comment: String?,
    @InjectedParam val context: Int?,
    @InjectedParam val commentsSettings: CommentsSettings,
) : ViewModel() {

    var name: Fullname = repository.getPostId(permalink)
    val post: StateFlow<PostData?> =
        repository.post.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val accounts: Flow<List<RedditAccount>> = accountsStorage.accounts

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()
    val dialogState: MutableStateFlow<Dialog?> = MutableStateFlow(null)

    private val replyState = MutableStateFlow<Fullname?>(null)
    val reply: StateFlow<Fullname?> = replyState.asStateFlow()

    val listState = LazyListState()

    val rules = flow {
        val rules = repository.getRules().getOrNull() ?: Rules()
        emit(rules)
    }.stateIn(viewModelScope, SharingStarted.Lazily, Rules())


    private var _newComments: MutableStateFlow<Set<Fullname>> = MutableStateFlow(emptySet())
    val newComments: StateFlow<Set<Fullname>> = _newComments.asStateFlow()
    private val _comments = repository.comments

    @OptIn(ExperimentalCoroutinesApi::class)
    val comments: StateFlow<List<CommentType>> = _comments.flatMapLatest { names ->
        val names = names.map { it.name }
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

    val depths = repository.depths.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())


    fun getNumberOfChildren(name: Fullname): Int {
        val startIndex = _comments.value.indexOfFirst { it.name == name }
        if (startIndex < 0) return 0
        val startDepth = _comments.value.drop(startIndex).first().depth
        return _comments.value.drop(startIndex + 1).takeWhile { it.depth > startDepth }
            .count()
    }

    private var _openComment = MutableStateFlow<Fullname?>(null)

    /** Name of the comment of which the bottom bar is currently open */
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

    private val _sort = MutableStateFlow<Sort?>(null)
    val sort: StateFlow<Sort?> = _sort.asStateFlow()

    init {
        _sort.value =
            if (commentsSettings.useRecommendedSort) null else commentsSettings.preferredSort
        fetchComments()
    }

    private suspend fun fetchAsync() {
        _loadingState.update { LoadingState.Loading }
        repository.getComments(
            permalink,
            sort = _sort.value,
            comment = comment,
            context = context
        )
        _sort.value = _sort.value ?: post.value?.suggestedSort
        _loadingState.update { LoadingState.Success }
    }

    suspend fun collapseAutomod() {
        if (commentsSettings.collapseAutoMod) {
            val automodComments =
                comments.value.filter { c ->
                    c is CommentType.Comment &&
                            c.comment.author.username == "AutoModerator"
                }
            for (comment in automodComments) {
                val comment = comment as CommentType.Comment
                repository.updateComment(
                    name,
                    CommentType.Comment(comment = comment.comment.copy(collapsed = true))
                )
            }
        }
    }

    private var scrollRestored = false
    suspend fun restoreScroll() {
        if (!scrollRestored) {
            val entity = historyManager.history.getPost(name)
            if (entity?.focusedComment != null) {
                val index =
                    comments.value.indexOfFirst { it.name == entity.focusedComment }
                if (index > 0) {
                    listState.scrollToItem(index)
                }
            }
            scrollRestored = true
        }
    }

    suspend fun highlightNewComments() {
        val entity = historyManager.history.getPost(name)
        if (entity == null) return
        // If the post was just loaded for the first time, skip highlighting new comments
        if (entity.comments == null) return
        val new = _comments.value.map { it.name }.toSet().toMutableSet()
        new -= entity.comments.toSet()
        _newComments.value = new
    }

    fun fetchComments() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAsync()
            collapseAutomod()
            restoreScroll()
            highlightNewComments()
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


    fun move(offset: Int) {
        val index = listState.firstVisibleItemIndex
        if (index + offset < 0 || index + offset >= comments.value.count()) return
        listState.requestScrollToItem(index + offset)
    }

    fun nextTopLevel() {
        val index = listState.firstVisibleItemIndex
        val comments = comments.value.toList()
        val nextIndex = comments.drop(index + 1).indexOfFirst { it.depth == 0 }
        if (nextIndex == -1) return
        listState.requestScrollToItem(index + 1 + nextIndex)
    }

    fun previousTopLevel() {
        val index = listState.firstVisibleItemIndex
        val comments = comments.value.toList()
        val previousIndex = comments.take(index).indexOfLast { it.depth == 0 }
        if (previousIndex == -1) return
        listState.requestScrollToItem(previousIndex)
    }

    private fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upvote(name)
        }
    }

    private fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.downvote(name)
        }
    }

    private fun save(name: Fullname, upvote: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.save(name, upvote)
        }
    }

    private fun unsave(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unsave(name)
        }
    }

    fun onEvent(event: CommentEvent) {
        when (event) {
            is VotableEvent.Upvote -> upvote(event.name)
            is VotableEvent.Downvote -> downvote(event.name)
            is VotableEvent.Save -> {
                if (event.target) {
                    save(event.name, event.upvote)
                } else {
                    unsave(event.name)
                }
            }

            is CommentEvent.Collapse -> collapse(event.name, event.target)
            is CommentEvent.ShowBar -> {
                if (event.name != null) {
                    toggleComment(event.name)
                } else {
                    _openComment.value = null
                }
            }

            is VotableEvent.Reply -> {
                replyTo(event.name)
                dialogState.value = Dialog.Reply(event.name)
            }

            is VotableEvent.ShowEdit -> dialogState.value = Dialog.Edit(event.name)
            is VotableEvent.ShowReport -> dialogState.value = Dialog.Report(event.name)
            is VotableEvent.ShowShare -> dialogState.value = Dialog.Share(event.name)
            is VotableEvent.ShowUser -> dialogState.value = Dialog.User(event.username)
            is CommentEvent.ShowCopy -> dialogState.value = Dialog.Copy(event.name)
        }
    }

    private fun collapse(
        name: Fullname,
        target: Boolean?,
    ) {
        viewModelScope.launch {
            repository.collapseComment(name, target)
        }
    }

    fun get(name: Fullname): Flow<CommentType?> {
        return commentsRepository.get(name)
    }

    fun report(name: Fullname, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.report(name, reason)
        }
    }

}
