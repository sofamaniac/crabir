package com.sofamaniac.crabir.domain.repository.thread

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.reddit.MoreResponseOuter
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.data.remote.reddit.commentSubmissionBody
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.FullnameKind
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.koin.core.annotation.ViewModelScope

interface ThreadRepository {

    val comments: StateFlow<Iterable<CommentType>>
    val post: Flow<PostData?>
    suspend fun getComments(
        permalink: String,
        sort: Sort? = null,
        timeframe: Timeframe? = null,
        comment: String? = null,
        context: Int? = null,
    )

    suspend fun getPost(name: Fullname): PostData?
    suspend fun getMoreComments(more: CommentType.More): Result<Unit>

    /** Submit a reply
     * @param parentId The name of the parent to the reply
     * @param comment The text of the comment
     * @param account The account to use to submit the comment, if null the current active account will be used
     */
    suspend fun postComment(
        parentId: Fullname,
        comment: String,
        account: RedditAccount?,
    ): Result<MoreResponseOuter>

    suspend fun updateComment(name: Fullname, value: CommentType)
    suspend fun collapseComment(name: Fullname, collapsed: Boolean?)

    /** Extract fullname from post permalink. */
    fun getPostId(permalink: String): Fullname


    fun refresh()

    /** Insert a comment into the thread
     * @param parent The name of the parent comment
     * @param comment The data of the comment
     */
    suspend fun insertReply(parent: Fullname, comment: CommentType)

    suspend fun upvote(name: Fullname): Result<Unit>
    suspend fun downvote(name: Fullname): Result<Unit>
    suspend fun save(name: Fullname, upvote: Boolean): Result<Unit>
    suspend fun unsave(name: Fullname): Result<Unit>

    suspend fun getRules(): Result<Rules>
    suspend fun report(name: Fullname, reason: String): Result<Unit>

}

@ViewModelScope
class ThreadRepositoryImpl(
    private val repository: CommentsRepository,
    private val postsRepository: LinksRepository,
    private val api: RedditAPIService,
) : ThreadRepository {

    private var _post: PostData? = null


    private val _postNameFlow: MutableStateFlow<Fullname?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val post: Flow<PostData?> = _postNameFlow.flatMapLatest { name ->
        if (name == null) {
            flowOf(null)
        } else {
            postsRepository.get(name)
        }
    }
    private val forest: MutableStateFlow<Forest> = MutableStateFlow(Forest.new(Fullname("")))
    override val comments: StateFlow<Iterable<CommentType>> = forest

    suspend fun fetchThread(
        permalink: String,
        sort: Sort? = null,
        comment: String? = null,
        context: Int? = null,
    ) = withContext(Dispatchers.IO) {
        if (_post != null && forest.value.isNotEmpty()) {
            Log.d("ThreadRepository", "fetchThread: post already loaded")
            return@withContext
        }
        _postNameFlow.value = getPostId(permalink)
        val response = api.getThread(permalink, sort = sort, comment = comment, context = context)
        if (response.isSuccess) {
            val body = response.getOrNull()
            if (body != null) {
                val data = body.post.data.children.first()
                _post = PostDataMapper.map(data.data)
                val newForest = Forest.from(
                    body.comments.data.children,
                    _post!!.name
                )
                val list = newForest.toList()
                repository.insert(list)
                forest.value = newForest
                postsRepository.insert(listOf(_post!!))
                _postNameFlow.value = _post!!.name
            }
        } else {
            Log.e("ThreadRepository", "fetchThread: ${response.exceptionOrNull()}")
        }
    }

    override suspend fun getPost(name: Fullname): PostData? {
        if (_post != null) {
            return _post
        } else {
            _post = postsRepository.getAsync(name)
        }
        return _post
    }

    override suspend fun getComments(
        permalink: String,
        sort: Sort?,
        timeframe: Timeframe?,
        comment: String?,
        context: Int?,
    ) {
        fetchThread(permalink, sort, comment = comment, context = context)
    }

    override fun getPostId(permalink: String): Fullname {
        val segments = permalink.split("/")
        val index = segments.indexOf("comments")
        return Fullname("t3_${segments[index + 1]}")
    }

    override suspend fun getMoreComments(more: CommentType.More): Result<Unit> {
        val response = api.getMoreComments(
            _post!!.name,
            more.data.children.take(100).joinToString(",")
        )
        if (response.isSuccess) {
            val body = response.getOrNull()
            if (body != null) {
                val things = body.json.data?.things ?: return Result.failure(
                    Exception(
                        body.json.errors?.fold(
                            ""
                        ) { acc, list -> acc + list.joinToString() }
                    )
                )
                val temp = Forest.from(things, root = more.data.name, depth = more.depth)
                val list = temp.toList()
                repository.insert(list)
                forest.update { forest ->
                    Log.d("ThreadRepository", "getMoreComments update forest")
                    forest.insert(more.data.name, temp, keepBase = false)
                }
            }
        }
        return Result.success(Unit)
    }

    override suspend fun postComment(
        parentId: Fullname,
        comment: String,
        account: RedditAccount?,
    ): Result<MoreResponseOuter> {
        val body = commentSubmissionBody(parentId, comment)
        return api.submitComment(body, account)
    }

    override suspend fun updateComment(
        name: Fullname,
        value: CommentType,
    ) {
        repository.update(value)
    }

    override suspend fun insertReply(parent: Fullname, comment: CommentType) {
        repository.insert(comment)
        forest.update { forest ->
            forest.insert(parent, Forest.from(comment))
        }
    }

    override fun refresh() {
        //comments = emptyList()
        forest.value = Forest.new(Fullname(""))
    }

    override suspend fun upvote(name: Fullname): Result<Unit> {
        return when (name.kind) {
            FullnameKind.Post -> postsRepository.upvote(name)
            FullnameKind.Comment -> repository.upvote(name)
            else -> Result.failure(Exception("Invalid fullname kind"))
        }
    }

    override suspend fun downvote(name: Fullname): Result<Unit> {
        return when (name.kind) {
            FullnameKind.Post -> postsRepository.downvote(name)
            FullnameKind.Comment -> repository.downvote(name)
            else -> Result.failure(Exception("Invalid fullname kind"))
        }
    }

    override suspend fun save(name: Fullname, upvote: Boolean): Result<Unit> {
        return when (name.kind) {
            FullnameKind.Post -> postsRepository.save(name, upvote)
            FullnameKind.Comment -> repository.save(name, upvote)
            else -> Result.failure(Exception("Invalid fullname kind"))
        }
    }

    override suspend fun unsave(name: Fullname): Result<Unit> {
        return when (name.kind) {
            FullnameKind.Post -> postsRepository.unsave(name)
            FullnameKind.Comment -> repository.unsave(name)
            else -> Result.failure(Exception("Invalid fullname kind"))
        }
    }

    override suspend fun collapseComment(name: Fullname, collapsed: Boolean?) {
        val commentOpt = repository.get(name).first() as? CommentType.Comment?
        if (commentOpt == null) {
            Log.e("ThreadRepository", "Comment not found")
            return
        }
        val comment = commentOpt.comment
        val newComment =
            commentOpt.copy(comment = comment.copy(collapsed = collapsed ?: !comment.collapsed))
        repository.update(newComment)
    }

    override suspend fun getRules(): Result<Rules> = withContext(Dispatchers.IO) {
        if (_post == null) {
            Result.failure(Exception("No subreddit"))
        } else {
            val subreddit = _post!!.subreddit
            api.getRules(subreddit.subredditPrefixed)
        }
    }

    override suspend fun report(name: Fullname, reason: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            api.report(name, reason)
        }
}

