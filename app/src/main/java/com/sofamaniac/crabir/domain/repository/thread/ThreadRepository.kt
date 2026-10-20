package com.sofamaniac.crabir.domain.repository.thread

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.reddit.MoreResponseOuter
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.commentSubmissionBody
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.domain.repository.CommentsCacheScope
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped

interface ThreadRepository {

    val comments: StateFlow<Iterable<CommentType>>
    val post: Flow<PostData?>
    val depths: Flow<Map<Fullname, Int>>
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

    /** Extract fullname from post permalink. */
    fun getPostId(permalink: String): Fullname


    fun refresh()

    /** Insert a comment into the thread
     * @param parent The name of the parent comment
     * @param comment The data of the comment
     */
    suspend fun insertReply(parent: Fullname, comment: CommentType)

}

@Scoped
@Scope(CommentsCacheScope::class)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override val depths: Flow<Map<Fullname, Int>> = forest.flatMapLatest { flowOf(it.depth) }


    suspend fun fetchThread(
        permalink: String,
        sort: Sort? = null,
        comment: String? = null,
        context: Int? = null,
    ) {
        if (_post != null && forest.value.isNotEmpty()) {
            Log.d("ThreadRepository", "fetchThread: post already loaded")
            return
        }
        _postNameFlow.value = getPostId(permalink)
        val response = api.getThread(permalink, sort = sort, comment = comment, context = context)
        if (response.isSuccess) {
            val body = response.getOrNull()
            if (body != null) {
                val data = body.post.data.children.first()
                _post = PostDataMapper.map(data.data)
                val forest = Forest.from(
                    body.comments.data.children,
                    _post!!.name
                )
                val list = forest.toList()
                repository.insert(list)
                this.forest.value = forest
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
}

