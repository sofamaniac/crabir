package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.reddit.MoreResponseOuter
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.postCommentBody
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Response

interface ThreadRepository {

    val comments: StateFlow<Iterable<CommentType>>
    suspend fun getComments(
        permalink: String,
        sort: Sort? = null,
        timeframe: Timeframe? = null,
        comment: String? = null,
        context: Int? = null,
    ): Unit

    suspend fun getPost(name: Fullname): PostData?
    suspend fun getMoreComments(more: CommentType.More): Unit

    suspend fun postComment(parentId: Fullname, comment: String): Response<MoreResponseOuter>

    suspend fun updateComment(name: Fullname, value: CommentType)

    /** Extract fullname from post permalink. */
    fun getPostId(permalink: String): Fullname


    fun refresh()

    suspend fun upvote(fullname: Fullname)
    suspend fun neutralVote(fullname: Fullname)
    suspend fun downvote(fullname: Fullname)
    suspend fun save(fullname: Fullname)
    suspend fun unsave(fullname: Fullname)

}

class Forest private constructor(
    val comments: Map<Fullname, CommentType>,
    val next: Map<Fullname, Fullname>,
    val root: Fullname
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

    fun insert(
        root: Fullname,
        l: List<Thing>,
        initialCapacity: Int,
        removeRoot: Boolean = false
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
        return object : Iterator<CommentType> {
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
    }
}

class ThreadRepositoryImpl @Inject constructor(
    val api: RedditAPIService,
    val visitedPostsDao: VisitedPostsDao,
    val commentsRepository: CommentsRepository,
    val postsRepository: LinksRepository,
) :
    ThreadRepository {
    private var post: PostData? = null
    private val forest: MutableStateFlow<Forest> = MutableStateFlow(Forest.empty())
    override val comments: StateFlow<Iterable<CommentType>> = forest


    suspend fun fetchThread(
        permalink: String,
        sort: Sort? = null,
        comment: String? = null,
        context: Int? = null
    ) {
        if (post != null && forest.value.comments.isNotEmpty()) {
            return
        }
        val response = api.getThread(permalink, sort = sort, comment = comment, context = context)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val data = body.post.data.children.first()
                post = PostDataMapper.map(data.data)
                forest.update {
                    Forest.create(
                        body.comments.data.children,
                        post!!.name,
                        post!!.numComments
                    )
                }
                commentsRepository.insert(forest.value)
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
        Log.d("ThreadRepositoryImpl", "forest: ${forest.value.next}")
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
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val things = body.json.data.things
                forest.value =
                    forest.value.insert(more.data.name, things, things.size, removeRoot = true)
            }
        }
    }

    override suspend fun postComment(
        parentId: Fullname,
        comment: String
    ): Response<MoreResponseOuter> {
        val body = postCommentBody(parentId, comment)
        return api.postComment(body)
    }

    override suspend fun updateComment(
        name: Fullname,
        value: CommentType
    ) {
        forest.update {
            it.updateComment(name, value)
        }
        commentsRepository.update(name, value)
    }

    override fun refresh() {
        //comments = emptyList()
        forest.value = Forest.empty()
    }

    override suspend fun upvote(fullname: Fullname) {
        commentsRepository.upvote(fullname)
    }

    override suspend fun neutralVote(fullname: Fullname) {
        commentsRepository.vote(fullname, null)
    }

    override suspend fun downvote(fullname: Fullname) {
        commentsRepository.downvote(fullname)
    }

    override suspend fun save(fullname: Fullname) {
        commentsRepository.save(fullname)
    }

    override suspend fun unsave(fullname: Fullname) {
        commentsRepository.unsave(fullname)
    }

}

//fun List<CommentType>.replaceMore(
//    more: CommentType.More,
//    children: List<CommentType>
//): List<CommentType> {
//    val withoutMore = filter {
//        when (it) {
//            is CommentType.Comment -> true
//            is CommentType.More -> it.name != more.name
//        }
//    }
//    return children.fold(withoutMore) { acc, c ->
//        val res = acc.insertComment(c)
//        if (!res.second) {
//            acc + c
//        } else {
//            res.first
//        }
//    }
//}
//
//fun CommentType.Comment.replaceMore(
//    more: CommentType.More,
//    replies: List<CommentType>
//): CommentType {
//    val withoutMore = comment.replies.filter {
//        when (it) {
//            is CommentType.Comment -> true
//            is CommentType.More -> it.name != more.name
//        }
//    }
//    return replies.fold(
//        this.copy(comment = comment.copy(replies = withoutMore)) as CommentType,
//    ) { acc, reply ->
//        acc.insertReply(reply)
//    }
//}
//
//fun CommentType.insertReply(reply: CommentType): CommentType {
//    when (this) {
//        is CommentType.More -> {
//            return this
//        }
//
//        is CommentType.Comment -> {
//            val replies: List<CommentType> = if (reply.parentId == this.name) {
//                comment.replies + reply
//            } else {
//                comment.replies.map { it.insertReply(reply) }
//            }
//            return this.copy(comment = comment.copy(replies = replies))
//        }
//    }
//}
//
///** Try to insert the comment into the list given in respect with `parentId`,
// * the boolean is true when the element was inserted */
//fun List<CommentType>.insertComment(
//    comment: CommentType
//): Pair<List<CommentType>, Boolean> {
//    var inserted = false
//    val result = this.map { c ->
//        if (c is CommentType.Comment) {
//            if (c.name == comment.parentId) {
//                val children = c.comment.replies + comment
//                inserted = true
//                c.copy(comment = c.comment.copy(replies = children))
//            } else {
//                val children = c.comment.replies.insertComment(comment)
//                inserted = inserted || children.second
//                c.copy(comment = c.comment.copy(replies = children.first))
//            }
//        } else {
//            c
//        }
//    }
//    return Pair(result, inserted)
//}
