/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.model.VotableData
import com.sofamaniac.reboost.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

interface FeedRepository {
    suspend fun getPosts(
        after: String,
        sort: Sort,
        timeframe: Timeframe? = null
    ): PagedResponse<String>

    suspend fun upvote(id: String): Result<Unit>
    suspend fun downvote(id: String): Result<Unit>
    suspend fun save(id: String): Result<Unit>
    suspend fun unsave(id: String): Result<Unit>
}

abstract class FeedRepositoryCommon(
    val postRepository: PostRepository,
    val api: RedditAPIService,
) : FeedRepository {

    private var _seenPosts: Set<String> = emptySet()

    fun refresh() {
        _seenPosts = emptySet()
    }

    fun observePost(id: String): Flow<VotableData> {
        return postRepository.observePost(id)
    }

    override suspend fun upvote(id: String): Result<Unit> {
        return postRepository.upvote(id)
    }

    override suspend fun downvote(id: String): Result<Unit> {
        return postRepository.downvote(id)
    }

    override suspend fun save(id: String): Result<Unit> {
        return postRepository.save(id)
    }

    override suspend fun unsave(id: String): Result<Unit> {
        return postRepository.unsave(id)
    }

    protected suspend fun <T : Thing> makeRequest(
        request: suspend () -> Response<Thing.Listing<T>>
    ): PagedResponse<String> {
        val response = request()
        if (response.isSuccessful) {
            Log.d("makeRequest", "code ${response.code()}")
            val listing = response.body()
            listing?.let {
                val posts = listing.data.children.map { thing ->
                    when (thing) {
                        is Thing.Post -> {
                            PostDataMapper.map(thing.data)
                        }

                        is Thing.Comment -> {
                            CommentDataMapper.map(thing.data)
                        }

                        else -> {
                            throw IllegalArgumentException("Unreachable code")
                        }
                    }
                }.filter { thing ->
                    !_seenPosts.contains(thing.id)
                }
                postRepository.addPosts(posts)
                posts.forEach { data ->
                    _seenPosts += data.id
                }
                val postsIds = posts.map { post ->
                    post.id
                }
                return PagedResponse(
                    data = postsIds,
                    after = it.data.after,
                    total = it.size
                )
            }
            return PagedResponse()
        }
        Log.e("makeRequest", "Error making request : ${response.errorBody()}")
        return PagedResponse()
    }
}

class PostsSource(
    private val repository: FeedRepositoryCommon,
    private val sort: Sort,
    private val timeframe: Timeframe?,
) : PagingSource<String, VotableData>() {


    override fun getRefreshKey(state: PagingState<String, VotableData>): String {
        return ""
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, VotableData> {
        val postsId = if (params.key != null) {
            getPosts(params.key!!, sort, timeframe)
        } else {
            PagedResponse()
        }
        val posts = postsId.data.map { id ->
            repository.observePost(id).first()
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = postsId.after,
            data = posts
        )
    }

    private suspend fun getPosts(
        after: String,
        sort: Sort = Sort.Best,
        timeframe: Timeframe? = null
    ): PagedResponse<String> {
        return repository.getPosts(after, sort, timeframe)
    }

}