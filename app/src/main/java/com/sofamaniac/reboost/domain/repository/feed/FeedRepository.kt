/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.VotableData
import com.sofamaniac.reboost.domain.repository.ListingRepository
import com.sofamaniac.reboost.domain.repository.ListingSource
import com.sofamaniac.reboost.domain.repository.VotableRepository
import kotlinx.coroutines.flow.Flow

interface FeedRepository<Params> {
//    suspend fun getPosts(
//        after: String,
//        params: Params,
//    ): PagedResponse<String>

    suspend fun upvote(id: String): Result<Unit>
    suspend fun downvote(id: String): Result<Unit>
    suspend fun save(id: String): Result<Unit>
    suspend fun unsave(id: String): Result<Unit>
}

abstract class FeedRepositoryCommon<Params>(
    val votableRepository: VotableRepository,
    val api: RedditAPIService,
) : FeedRepository<Params>, ListingRepository<Params, VotableData>() {

    fun observePost(id: String): Flow<VotableData?> {
        return votableRepository.observePost(id)
    }

    override suspend fun upvote(id: String): Result<Unit> {
        return votableRepository.upvote(id)
    }

    override suspend fun downvote(id: String): Result<Unit> {
        return votableRepository.downvote(id)
    }

    override suspend fun save(id: String): Result<Unit> {
        return votableRepository.save(id)
    }

    override suspend fun unsave(id: String): Result<Unit> {
        return votableRepository.unsave(id)
    }

    override fun thingToData(thing: Thing): VotableData? {
        return when (thing) {
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
    }

    override fun onResponseSuccess(things: List<Thing>) {
        super.onResponseSuccess(things)
        val votableList = things.mapNotNull { thingToData(it) }
        votableRepository.addPosts(votableList)
    }
}

data class FeedParams(val sort: Sort, val timeframe: Timeframe?)

typealias FeedSource<Params> = ListingSource<Params, VotableData>


//class FeedSource<Params>(
//    private val repository: FeedRepositoryCommon<Params>,
//    private val params: Params,
//) : PagingSource<String, VotableData>() {
//
//
//    override fun getRefreshKey(state: PagingState<String, VotableData>): String {
//        return ""
//    }
//
//    override suspend fun load(params: LoadParams<String>): LoadResult<String, VotableData> {
//        val postsId = if (params.key != null) {
//            getPosts(params.key!!)
//        } else {
//            PagedResponse()
//        }
//        val posts = postsId.data.map { id ->
//            repository.observePost(id).first()
//        }
//        return LoadResult.Page(
//            prevKey = null,
//            nextKey = postsId.after,
//            data = posts
//        )
//    }
//
//    private suspend fun getPosts(
//        after: String,
//    ): PagedResponse<String> {
//        return repository.getPosts(after, params)
//    }
//
//}