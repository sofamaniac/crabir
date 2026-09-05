package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.reddit.MoreResponseOuter
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.commentSubmissionBody
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.ViewModelScope

interface ThreadRepository {

    val comments: StateFlow<Iterable<Fullname>>
    suspend fun getComments(
        permalink: String,
        sort: Sort? = null,
        timeframe: Timeframe? = null,
        comment: String? = null,
        context: Int? = null,
    )

    suspend fun getPost(name: Fullname): PostData?
    suspend fun getMoreComments(more: CommentType.More)

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
    fun insertReply(parent: Fullname, comment: CommentType)
}

@ViewModelScope
class ThreadRepositoryNew(
    private val repository: CommentsRepository,
    private val postsRepository: LinksRepository,
    private val api: RedditAPIService,
) :
    ThreadRepository {

    private var post: PostData? = null
    private val forest: MutableStateFlow<List<Fullname>> = MutableStateFlow(emptyList())
    override val comments: StateFlow<Iterable<Fullname>> = forest


    suspend fun fetchThread(
        permalink: String,
        sort: Sort? = null,
        comment: String? = null,
        context: Int? = null,
    ) {
        if (post != null && forest.value.isNotEmpty()) {
            return
        }
        val response = api.getThread(permalink, sort = sort, comment = comment, context = context)
        if (response.isSuccess) {
            val body = response.getOrNull()
            if (body != null) {
                val data = body.post.data.children.first()
                post = PostDataMapper.map(data.data)
                val forest =
                    Forest.create(
                        body.comments.data.children,
                        post!!.name,
                        post!!.numComments
                    )
                repository.insert(forest)
                this.forest.update {
                    forest.iterator().asSequence().map { it.name }.toList()
                }
                postsRepository.insert(listOf(post!!))
            }
        }
    }

    override suspend fun getPost(name: Fullname): PostData? {
        if (post != null) {
            return post
        } else {
            post = postsRepository.getValue(name)
        }
        return post
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

    override suspend fun getMoreComments(more: CommentType.More) {
        val response = api.getMoreComments(
            post!!.name,
            more.data.children.take(100).joinToString(",")
        )
        if (response.isSuccess) {
            val body = response.getOrNull()
            if (body != null) {
                // TODO display error
                val things = body.json.data?.things ?: return
                val temp = Forest.create(things, Fullname(""), things.size)
                repository.insert(temp)
                forest.update { list ->
                    val startIndex = list.indexOfFirst { it == more.data.name }
                    val head = runCatching { list.subList(0, startIndex) }.getOrDefault(emptyList())
                    val tail = runCatching { list.subList(startIndex + 1, list.size) }.getOrDefault(
                        emptyList()
                    )
                    head + temp.asSequence().map { it.name } + tail
                }
            }
        }
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
        repository.update(name, value)
    }

    override fun insertReply(parent: Fullname, comment: CommentType) {
        forest.update { list ->
            val index = list.indexOfFirst { it == parent }
            list.subList(0, index + 1) + comment.name + list.subList(index + 1, list.size)
        }
        repository.insert(comment)
    }

    override fun refresh() {
        //comments = emptyList()
        forest.value = emptyList()
    }
}

class Forest private constructor(
    val comments: Map<Fullname, CommentType>,
    val next: Map<Fullname, Fullname>,
    val root: Fullname,
) : Iterable<CommentType> {
    companion object {
        fun empty(root: Fullname = Fullname("EmptyRoute")): Forest {
            return Forest(emptyMap(), emptyMap(), root)
        }

        fun create(l: List<Thing>, root: Fullname, initialCapacity: Int): Forest {
            return empty(root).insert(root, l, initialCapacity)
        }
    }

    fun updateComment(name: Fullname, value: CommentType): Forest {
        val newTable = comments + (
                name to value
                )
        return Forest(newTable, next, root)
    }

    fun insertReply(
        root: Fullname,
        comment: CommentType,
    ): Forest {
        val comments = this.comments.toMutableMap()
        val next = this.next.toMutableMap()
        val endNext = next[root]
        next[root] = comment.name
        comments[comment.name] = comment
        endNext?.let {
            next[comment.name] = it
        }
        return Forest(comments, next, this.root)
    }

    fun insert(
        root: Fullname,
        l: List<Thing>,
        initialCapacity: Int,
        removeRoot: Boolean = false,
    ): Forest {
        val stack = ArrayDeque<Thing>(initialCapacity)
        val comments = this.comments.toMutableMap()
        val next = this.next.toMutableMap()
        for (comment in l.reversed()) {
            stack.addLast(comment)
        }
        var lastSeen: Fullname = root
        val endNext = next[root]
        if (removeRoot) {
            lastSeen = next.entries.find { (_, value) -> value == root }!!.key
            comments.remove(root)
        }
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            val comment = when (current) {
                is Thing.Comment -> CommentType.Comment(CommentDataMapper.map(current.data))
                is Thing.More -> CommentType.More(current.data)
                else -> continue
            }
            next[lastSeen] = current.name
            comments[current.name] = comment
            lastSeen = current.name
            if (current is Thing.Comment) {
                for (child in current.data.replies.reversed()) {
                    stack.addLast(child)
                }
            }
        }
        endNext?.let {
            next[lastSeen] = it
        }
        return Forest(comments, next, this.root)
    }

    override fun iterator(): Iterator<CommentType> {
        val iterator = object : Iterator<CommentType> {

            var current = this@Forest.root
            override fun next(): CommentType {
                val next = this@Forest.next[current]
                if (next == null) throw NoSuchElementException()
                current = next
                return this@Forest.comments[next] ?: throw NoSuchElementException()
            }

            override fun hasNext(): Boolean {
                return this@Forest.next[current] != null
            }
        }

        return iterator
    }
}
