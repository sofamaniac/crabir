package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.parseMarkdownFlow
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.ThreadRepository
import com.sofamaniac.crabir.ui.post.PostViewModelInterface
import com.sofamaniac.redditmarkdown.redditFlavour.RedditFlavourDescriptor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel(assistedFactory = ThreadViewModel.Factory::class)
class ThreadViewModel @AssistedInject constructor(
    private val repository: ThreadRepository,
    private val visitedPostsDao: VisitedPostsDao,
    private val linksRepository: LinksRepository,
    @Assisted("permalink") val permalink: String,
    @Assisted("comment") val comment: String?,
    @Assisted val context: Int?,
    @Assisted val initialSort: Sort?,
) : ViewModel(), CommentViewModelInterface, PostViewModelInterface {

    var name: Fullname = repository.getPostId(permalink)

    override val likes: Flow<Boolean?> = flowOf(null)
    override val saved: Flow<Boolean> = flowOf(false)
    override val rules: StateFlow<Rules>
        get() = TODO("Not yet implemented")

    private val replyState = MutableStateFlow<Fullname?>(null)
    val reply: StateFlow<Fullname?> = replyState.asStateFlow()

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    //private var _comments = MutableStateFlow<List<CommentType>>(emptyList())
    //val comments: StateFlow<List<CommentType>> = _comments.asStateFlow()
    private var _post = MutableStateFlow<PostData?>(null)
    override val post: Flow<PostData?> = _post.asStateFlow()
    val comments = repository.comments

    @OptIn(ExperimentalCoroutinesApi::class)
    override var markdown: StateFlow<State> = _post.flatMapLatest { post ->
        if (post == null) flowOf(State.Loading())
        else parseMarkdownFlow(post.body.markdown, flavour = RedditFlavourDescriptor(true))
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = State.Loading()
    )

    private val _markdownComments: MutableMap<Fullname, StateFlow<State>> = mutableMapOf()
    override fun getMarkdownState(name: Fullname): StateFlow<State> {
        if (!_markdownComments.contains(name)) {
            //val comment = _comments.value.findComment(name)!!
            val comment = comments.value.find { it.name == name } as CommentType.Comment
            _markdownComments[name] =
                parseMarkdownFlow(
                    comment.comment.body.markdown,
                    flavour = RedditFlavourDescriptor(true)
                )
                    .stateIn(
                        viewModelScope,
                        started = SharingStarted.Lazily,
                        initialValue = State.Loading()
                    )
        }
        return _markdownComments[name]!!
    }

    //override val post: StateFlow<PostData?> = _post.asStateFlow()
    override val flairs: StateFlow<List<FlairInfo>>
        get() = TODO("Not yet implemented")

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun unhide() {
        TODO("Not yet implemented")
    }

    override fun delete() {
        TODO("Not yet implemented")
    }

    override fun editFlair(flairId: String, text: String?) {
        TODO("Not yet implemented")
    }

    override fun getFlairs() {
        TODO("Not yet implemented")
    }

    override fun markNSFW() {
        TODO("Not yet implemented")
    }

    override fun unmarkNSFW() {
        TODO("Not yet implemented")
    }

    override fun markSpoiler() {
        TODO("Not yet implemented")
    }

    override fun unmarkSpoiler() {
        TODO("Not yet implemented")
    }

    override fun setInboxReplies(enabled: Boolean) {
        TODO("Not yet implemented")
    }

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

    private val _sort = MutableStateFlow<Sort?>(null)
    val sort: StateFlow<Sort?> = _sort.asStateFlow()

    init {
        _sort.value = initialSort
        fetchComments()
    }

    private fun getPost(): PostData? {
        val post = runBlocking(Dispatchers.IO) {
            val post = repository.getPost(name) ?: linksRepository.getValue(name)
            if (post == null) {
                Log.e("ThreadViewModel", "getPost: Post not found in database ($name)")
            }
            post
        }
        return post
    }

    fun collapseComment(name: Fullname, collapsed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            val comment =
                (comments.value.find { it.name == name } as CommentType.Comment).comment
            repository.updateComment(
                name,
                CommentType.Comment(comment.copy(collapsed = collapsed))
            )
        }
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

    override fun replyTo(name: Fullname?) {
        replyState.value = name
    }

    override fun submitComment(parent: Fullname, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.postComment(parent, body)
            if (!response.isSuccessful) {
                return@launch
            }
            val result = response.body()?.json ?: return@launch
            // TODO display error if any
            val comment = result.data?.things?.firstOrNull() ?: return@launch
            val commentDTO = comment as Thing.Comment
            var commentData = CommentDataMapper.map(commentDTO.data)
            commentData =
                commentData.copy(relationship = commentData.relationship.copy(liked = true))
            val p = comments.value.find { it.name == parent }
            commentData = commentData.copy(depth = (p?.depth ?: 0) + 1)
            repository.insertReply(parent, CommentType.Comment(commentData))
            replyState.value = null
        }
    }

    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = repository.comments.value.find { it.name == name }
            val likes = comment?.relationship?.liked
            if (likes != true) {
                repository.upvote(name)
            } else {
                repository.neutralVote(name)
            }
            val newLikes = if (likes != true) {
                true
            } else {
                null
            }
            val newComment =
                comment!!.copy(
                    relationship = comment.relationship.copy(
                        liked = newLikes
                    )
                ).updateScore(likes, newLikes)
            repository.updateComment(comment.name, newComment as CommentType)

        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = repository.comments.value.find { it.name == name }
            val likes = comment?.relationship?.liked
            if (likes != false) {
                repository.downvote(name)
            } else {
                repository.neutralVote(name)
            }
            val newLikes = if (likes != false) {
                false
            } else {
                null
            }
            val newComment =
                comment!!.copy(
                    relationship = comment.relationship.copy(
                        liked = newLikes
                    )
                ).updateScore(likes, newLikes)
            repository.updateComment(comment.name, newComment as CommentType)
        }
    }

    override fun save(name: Fullname, target: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                repository.save(name)
            } else {
                repository.unsave(name)
            }
            val comment = repository.comments.value.find { it.name == name }!!
            val newComment = comment.copy(
                relationship = comment.relationship.copy(
                    saved = target
                )
            )
            repository.updateComment(name, newComment as CommentType)
        }
    }

    fun visitPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = VisitedPostEntity(post.name, System.currentTimeMillis(), visitedBy)
            visitedPostsDao.insert(entity)
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
        fun create(
            @Assisted("permalink") permalink: String,
            @Assisted("comment") comment: String? = null,
            context: Int? = null,
            initialSort: Sort?,
        ): ThreadViewModel
    }
}

//fun List<CommentType>.updateComment(
//    name: Fullname,
//    update: (CommentType) -> CommentType
//): List<CommentType> {
//    return map { comment ->
//        when {
//            comment.name == name -> update(comment)
//            comment is CommentType.Comment ->
//                CommentType.Comment(
//                    comment.comment.updateReplies(
//                        replies = comment.comment.replies.updateComment(
//                            name,
//                            update
//                        )
//                    )
//                )
//
//            else -> comment
//        }
//    }
//
//}
//
//fun List<CommentType>.count(): Int {
//    return this.sumOf {
//        1 + when (it) {
//            is CommentType.Comment -> it.comment.replies.count()
//            is CommentType.More -> 1
//        }
//    }
//}
//
//fun List<CommentType>.findComment(name: Fullname): CommentType.Comment? {
//    for (comment in this) {
//        if (comment.name == name && comment is CommentType.Comment) {
//            return comment
//        }
//    }
//    for (comment in this) {
//        if (comment is CommentType.Comment) {
//            val res = comment.comment.replies.findComment(name)
//            if (res != null) {
//                return res
//            }
//        }
//    }
//    return null
//}