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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel(assistedFactory = ThreadViewModel.Factory::class)
class ThreadViewModel @AssistedInject constructor(
    private val repository: ThreadRepository,
    private val visitedPostsDao: VisitedPostsDao,
    @Assisted("permalink") val permalink: String,
    @Assisted("comment") val comment: String?,
    @Assisted val context: Int?,
) : ViewModel(), CommentViewModelInterface {

    var name: Fullname = repository.getPostId(permalink)

    override val likes: Flow<Boolean?> = flowOf(null)
    override val saved: Flow<Boolean> = flowOf(false)

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var _comments = MutableStateFlow<List<CommentType>>(emptyList())
    val comments: StateFlow<List<CommentType>> = _comments.asStateFlow()

    private var _post = MutableStateFlow<PostData?>(null)
    val post: StateFlow<PostData?> = _post.asStateFlow()

    private var _openComment = MutableStateFlow<Fullname?>(null)

    /** Id of the comment of which the bottom bar is currently open */
    override val openComment: StateFlow<Fullname?> = _openComment.asStateFlow()

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
            _comments.value = repository.getComments(
                permalink,
                sort = _sort.value,
                comment = comment,
                context = context
            )
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

    override fun submitComment(parent: Fullname, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.postComment(parent, body)
            if (!response.isSuccessful) {
                return@launch
            }
            val comment = response.body()?.json?.data?.things?.firstOrNull() ?: return@launch
            val commentDTO = comment as Thing.Comment
            var commentData = CommentDataMapper.map(commentDTO.data)
            commentData =
                commentData.copy(relationship = commentData.relationship.copy(liked = true))
            if (parent == post.value?.name) {
                _comments.update {
                    it + CommentType.Comment(commentData.copy(depth = 0))
                }
            } else {
                _comments.update {
                    it.updateComment(parent) { c ->
                        c as CommentType.Comment
                        val replies =
                            c.comment.replies + CommentType.Comment(commentData.copy(depth = c.depth + 1))
                        c.copy(comment = c.comment.copy(replies = replies))
                    }
                }
            }
        }
    }

    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = comments.value.findComment(name)?.comment?.relationship?.liked
            if (likes != true) {
                repository.upvote(name)
            } else {
                repository.neutralVote(name)
            }
            _comments.update {
                it.updateComment(name) { c ->
                    val comment = (c as CommentType.Comment).comment
                    val newLikes = if (likes != true) {
                        true
                    } else {
                        null
                    }
                    CommentType.Comment(
                        comment.copy(
                            relationship = comment.relationship.copy(
                                liked = newLikes
                            )
                        ).updateScore(likes, newLikes)
                    )
                }
            }

        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = comments.value.findComment(name)?.comment?.relationship?.liked
            if (likes != false) {
                repository.downvote(name)
            } else {
                repository.neutralVote(name)
            }
            _comments.update {
                it.updateComment(name) { c ->
                    val comment = (c as CommentType.Comment).comment
                    val newLikes = if (likes != false) {
                        false
                    } else {
                        null
                    }
                    CommentType.Comment(
                        comment.copy(
                            relationship = comment.relationship.copy(
                                liked = newLikes
                            )
                        ).updateScore(likes, newLikes)
                    )
                }
            }
        }
    }

    override fun save(name: Fullname, target: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                repository.save(name)
            } else {
                repository.unsave(name)
            }
        }
        _comments.update {
            it.updateComment(name) { c ->
                val comment = (c as CommentType.Comment).comment
                CommentType.Comment(
                    comment.copy(
                        relationship = comment.relationship.copy(
                            saved = target
                        )
                    )
                )
            }
        }

    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("permalink") permalink: String,
            @Assisted("comment") comment: String? = null,
            context: Int? = null,
        ): ThreadViewModel
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

fun List<CommentType>.findComment(name: Fullname): CommentType.Comment? {
    for (comment in this) {
        if (comment.name == name && comment is CommentType.Comment) {
            return comment
        }
    }
    for (comment in this) {
        if (comment is CommentType.Comment) {
            val res = comment.comment.replies.findComment(name)
            if (res != null) {
                return res
            }
        }
    }
    return null
}