package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.toDomainModel
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.ThreadRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel(assistedFactory = ThreadViewModel.Factory::class)
class ThreadViewModel @AssistedInject constructor(
    private val repository: ThreadRepository,
    private val visitedPostsDao: VisitedPostsDao,
    @Assisted val permalink: String,
) : ViewModel() {

    var id: String = repository.getPostId(permalink)

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private var _comments = MutableStateFlow<List<CommentType>>(emptyList())
    val comments: StateFlow<List<CommentType>> = _comments.asStateFlow()

    private var _post = MutableStateFlow<PostData?>(null)
    val post: StateFlow<PostData?> = _post.asStateFlow()

    private var _openComment = MutableStateFlow<String?>(null)

    /** Id of the comment currently open */
    val openComment: StateFlow<String?> = _openComment.asStateFlow()

    /** If [openComment] is equal to [id], close it. Otherwise, open it. */
    fun toggleComment(id: String) {
        if (_openComment.value == id) {
            _openComment.value = null
        } else {
            _openComment.value = id
        }
    }

    private val _sort = MutableStateFlow(Sort.Best)
    val sort: StateFlow<Sort> = _sort.asStateFlow()

    init {
        // try initializing post
        _post.value = getPost()
        fetchComments()
    }

    private fun getPost(): PostData? {
        val post = runBlocking(Dispatchers.IO) {
            val post = repository.getPost(id) ?: visitedPostsDao.getPost(id)?.toDomainModel()
            if (post == null) {
                Log.e("ThreadViewModel", "getPost: Post not found in database ($id)")
            }
            post
        }
        return post
    }

    fun fetchComments() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _comments.value = repository.getComments(permalink, sort = _sort.value)
            // If post was not found set it here.
            _post.value = _post.value ?: getPost()
            _isRefreshing.value = false
        }
    }

    fun fetchMoreComments(more: CommentType.More) {
        viewModelScope.launch {
            _comments.value = repository.getMoreComments(more)
        }
    }

    fun setSort(sort: Sort) {
        _sort.value = sort
        refresh()
    }


    fun refresh() {
        repository.refresh()
    }

    fun upvote(name: String, likes: Boolean?) {
        viewModelScope.launch {
            try {
                if (likes != true) {
                    repository.upvote(name)
                } else {
                    repository.neutralVote(name)
                }
                _comments.update {
                    it.updateComment(name) { c ->
                        val comment = (c as CommentType.Comment).comment
                        CommentType.Comment(
                            comment.copy(
                                relationship = comment.relationship.copy(
                                    liked = if (likes != true) {
                                        true
                                    } else {
                                        null
                                    }
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ThreadViewModel", "upvote: $e")
            }
        }
    }

    fun downvote(name: String, likes: Boolean?) {
        viewModelScope.launch {
            try {
                if (likes != false) {
                    repository.downvote(name)
                } else {
                    repository.neutralVote(name)
                }
                _comments.update {
                    it.updateComment(name) { c ->
                        val comment = (c as CommentType.Comment).comment
                        CommentType.Comment(
                            comment.copy(
                                relationship = comment.relationship.copy(
                                    liked = if (likes != false) {
                                        false
                                    } else {
                                        null
                                    }
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ThreadViewModel", "downvote: $e")
            }
        }
    }

    fun save(name: String, saved: Boolean) {
        try {
            viewModelScope.launch {
                if (saved) {
                    repository.unsave(name)
                } else {
                    repository.save(name)
                }
            }
            _comments.update {
                it.updateComment(name) { c ->
                    val comment = (c as CommentType.Comment).comment
                    CommentType.Comment(
                        comment.copy(
                            relationship = comment.relationship.copy(
                                saved = !saved
                            )
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ThreadViewModel", "save: $e")
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(permalink: String): ThreadViewModel
    }
}

fun List<CommentType>.updateComment(
    name: String,
    update: (CommentType) -> CommentType
): List<CommentType> {
    return map { comment ->
        when {
            comment.name == name -> update(comment)
            comment is CommentType.Comment ->
                CommentType.Comment(
                    comment.comment.updateReplies(
                        replies = comment.comment.replies.updateComment(
                            name,
                            update
                        )
                    )
                )
            else -> comment
        }
    }

}