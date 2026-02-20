package com.sofamaniac.reboost.domain.repository

import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.remote.api.DOWNVOTED
import com.sofamaniac.reboost.data.remote.api.NEUTRAL
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.api.UPVOTED
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.reboost.data.remote.dto.comment.Sort
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.domain.model.CommentType
import com.sofamaniac.reboost.domain.model.PostData

interface ThreadRepository {
    suspend fun getComments(
        permalink: String,
        sort: Sort, timeframe: Timeframe? = null
    ): List<CommentType>

    suspend fun getPost(id: String): PostData?
    suspend fun getMoreComments(more: Thing.More): List<Thing>

    /** Extract id from post permalink. */
    fun getPostId(permalink: String): String

    fun refresh()

    suspend fun upvote(id: String)
    suspend fun neutralVote(id: String)
    suspend fun downvote(id: String)
    suspend fun save(id: String)
    suspend fun unsave(id: String)

}

class ThreadRepositoryImpl(
    val api: RedditAPIService,
    val visitedPostsDao: VisitedPostsDao,
    val postRepository: PostRepository
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
                        CommentType.More(comment as Thing.More)
                    }
                }
                postRepository.addPost(post!!)
            }
        }
    }

    override suspend fun getPost(id: String): PostData? {
        if (post != null) {
            return post
        } else {
            post = postRepository.getPost(id) as? PostData?
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

    override fun getPostId(permalink: String): String {
        val segments = permalink.split("/")
        val index = segments.indexOf("comments")
        return segments[index + 1]
    }

    override suspend fun getMoreComments(more: Thing.More): List<Thing.Comment> {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        comments = emptyList()
    }

    override suspend fun upvote(id: String) {
        api.vote(id, UPVOTED)
    }

    override suspend fun neutralVote(id: String) {
        api.vote(id, NEUTRAL)
    }

    override suspend fun downvote(id: String) {
        api.vote(id, DOWNVOTED)
    }

    override suspend fun save(id: String) {
        api.save(id)
    }

    override suspend fun unsave(id: String) {
        api.save(id)
    }

}

