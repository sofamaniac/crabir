package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.api.DOWNVOTED
import com.sofamaniac.crabir.data.remote.api.MoreResponseOuter
import com.sofamaniac.crabir.data.remote.api.NEUTRAL
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.api.UPVOTED
import com.sofamaniac.crabir.data.remote.api.postCommentBody
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.thread.updateComment
import retrofit2.Response
import javax.inject.Inject

interface ThreadRepository {
    suspend fun getComments(
        permalink: String,
        sort: Sort, timeframe: Timeframe? = null
    ): List<CommentType>

    suspend fun getPost(name: Fullname): PostData?
    suspend fun getMoreComments(more: CommentType.More): List<CommentType>

    suspend fun postComment(parentId: Fullname, comment: String): Response<MoreResponseOuter>

    /** Extract fullname from post permalink. */
    fun getPostId(permalink: String): Fullname


    fun refresh()

    suspend fun upvote(fullname: Fullname)
    suspend fun neutralVote(fullname: Fullname)
    suspend fun downvote(fullname: Fullname)
    suspend fun save(fullname: Fullname)
    suspend fun unsave(fullname: Fullname)

}

class ThreadRepositoryImpl @Inject constructor(
    val api: RedditAPIService,
    val visitedPostsDao: VisitedPostsDao,
    val commentsRepository: VotableRepository,
    val postsRepository: LinksRepository,
) :
    ThreadRepository {
    private var post: PostData? = null
    private var comments: List<CommentType> = emptyList()

    suspend fun fetchThread(permalink: String, sort: Sort) {
        if (post != null && comments.isNotEmpty()) {
            return
        }
        val response = api.getThread(permalink, sort = sort)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val data = body.post.data.children.first()
                post = PostDataMapper.map(data.data)
                for (comment in body.comments.data.children) {
                    comments += if (comment is Thing.Comment) {
                        CommentType.Comment(CommentDataMapper.map(comment.data))
                    } else {
                        CommentType.More((comment as Thing.More).data)
                    }
                }
                postsRepository.insert(listOf(post!!))
            }
        }
    }

    override suspend fun getPost(name: Fullname): PostData? {
        if (post != null) {
            return post
        } else {
            post = postsRepository.get(name) as? PostData?
        }
        return post
    }

    override suspend fun getComments(
        permalink: String,
        sort: Sort,
        timeframe: Timeframe?
    ): List<CommentType> {
        fetchThread(permalink, sort)
        return comments
    }

    override fun getPostId(permalink: String): Fullname {
        val segments = permalink.split("/")
        val index = segments.indexOf("comments")
        return Fullname("t3_${segments[index + 1]}")
    }

    override suspend fun getMoreComments(more: CommentType.More): List<CommentType> {
        val response = api.getMoreComments(
            post!!.name,
            more.data.children.take(100).joinToString(",")
        )
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val children = body.json.data.things.map { comment ->
                    if (comment is Thing.Comment) {
                        CommentType.Comment(CommentDataMapper.map(comment.data))
                    } else {
                        CommentType.More((comment as Thing.More).data)
                    }
                }
                comments = if (more.data.parentId == post!!.name) {
                    comments.replaceMore(more, children)
                } else {
                    comments.updateComment(more.data.parentId) { comment ->
                        if (comment is CommentType.Comment) {
                            val replies = comment.comment.replies.replaceMore(more, children)
                            comment.copy(
                                comment = comment.comment.copy(replies = replies)
                            )
                        } else {
                            comment
                        }
                    }
                }
            }
        }
        return comments
    }

    override suspend fun postComment(
        parentId: Fullname,
        comment: String
    ): Response<MoreResponseOuter> {
        val body = postCommentBody(parentId, comment)
        return api.postComment(body)
    }

    override fun refresh() {
        comments = emptyList()
    }

    override suspend fun upvote(fullname: Fullname) {
        api.vote(fullname, UPVOTED)
    }

    override suspend fun neutralVote(fullname: Fullname) {
        api.vote(fullname, NEUTRAL)
    }

    override suspend fun downvote(fullname: Fullname) {
        api.vote(fullname, DOWNVOTED)
    }

    override suspend fun save(fullname: Fullname) {
        api.save(fullname)
    }

    override suspend fun unsave(fullname: Fullname) {
        api.save(fullname)
    }

}

fun List<CommentType>.replaceMore(
    more: CommentType.More,
    children: List<CommentType>
): List<CommentType> {
    val withoutMore = filter {
        when (it) {
            is CommentType.Comment -> true
            is CommentType.More -> it.name != more.name
        }
    }
    return children.fold(withoutMore) { acc, c ->
        val res = acc.insertComment(c)
        if (!res.second) {
            acc + c
        } else {
            res.first
        }
    }
}

/** Try to insert the comment into the list given in respect with `parentId`,
 * the boolean is true when the element was inserted */
fun List<CommentType>.insertComment(
    comment: CommentType
): Pair<List<CommentType>, Boolean> {
    var inserted = false
    val result = this.map { c ->
        if (c is CommentType.Comment) {
            if (c.name == comment.parentId) {
                val children = c.comment.replies + comment
                inserted = true
                c.copy(comment = c.comment.copy(replies = children))
            } else {
                val children = c.comment.replies.insertComment(comment)
                inserted = inserted || children.second
                c.copy(comment = c.comment.copy(replies = children.first))
            }
        } else {
            c
        }
    }
    return Pair(result, inserted)
}
