package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.toDomainModel
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
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

    var name: Fullname = repository.getPostId(permalink)

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private var _comments = MutableStateFlow<List<CommentType>>(emptyList())
    val comments: StateFlow<List<CommentType>> = _comments.asStateFlow()

    private var _post = MutableStateFlow<PostData?>(null)
    val post: StateFlow<PostData?> = _post.asStateFlow()

    private var _openComment = MutableStateFlow<Fullname?>(null)

    /** Id of the comment of which the bottom bar is currently open */
    val openComment: StateFlow<Fullname?> = _openComment.asStateFlow()

    /** If [openComment] is equal to [name], close it. Otherwise, open it. */
    fun toggleComment(name: Fullname) {
        if (_openComment.value == name) {
            _openComment.value = null
        } else {
            _openComment.value = name
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
            val post = repository.getPost(name) ?: visitedPostsDao.getPost(name)?.toDomainModel()
            if (post == null) {
                Log.e("ThreadViewModel", "getPost: Post not found in database ($name)")
            }
            post
        }
        return post
    }

    fun collapseComment(name: Fullname, collapsed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _comments.update { comments ->
                comments.updateComment(name) {
                    val comment = (it as CommentType.Comment).comment
                    CommentType.Comment(comment.copy(collapsed = collapsed))
                }
            }
        }
    }

    fun fetchComments() {
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.value = true
            _comments.value = repository.getComments(permalink, sort = _sort.value)
            // If post was not found set it here.
            _post.value = _post.value ?: getPost()
            _isRefreshing.value = false
        }
    }

    fun fetchMoreComments(more: CommentType.More) {
        viewModelScope.launch(Dispatchers.IO) {
            _comments.value = repository.getMoreComments(more)
        }
    }

    fun setSort(sort: Sort) {
        _sort.value = sort
        refresh()
    }


    fun refresh() {
        repository.refresh()
        _comments.value = emptyList()
        fetchComments()
    }

    fun postComment(parentId: Fullname, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.postComment(parentId, comment)
            if (!response.isSuccessful) {
                return@launch
            }
            val comment = response.body()?.json?.data?.things?.firstOrNull() ?: return@launch
            val commentDTO = comment as Thing.Comment
            var commentData = CommentDataMapper.map(commentDTO.data)
            commentData =
                commentData.copy(relationship = commentData.relationship.copy(liked = true))
            if (parentId == post.value?.name) {
                _comments.update {
                    it + CommentType.Comment(commentData.copy(depth = 0))
                }
            } else {
                _comments.update {
                    it.updateComment(parentId) { c ->
                        c as CommentType.Comment
                        val replies =
                            c.comment.replies + CommentType.Comment(commentData.copy(depth = c.depth + 1))
                        c.copy(comment = c.comment.copy(replies = replies))
                    }
                }
            }
        }
    }

    fun upvote(name: Fullname, likes: Boolean?) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun downvote(name: Fullname, likes: Boolean?) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun save(name: Fullname, saved: Boolean) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
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
    name: Fullname,
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