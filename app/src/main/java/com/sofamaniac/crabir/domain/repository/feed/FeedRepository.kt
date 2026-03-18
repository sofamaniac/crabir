/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.ListingRepository
import com.sofamaniac.crabir.domain.repository.ListingSource
import com.sofamaniac.crabir.domain.repository.VotableRepository
import kotlinx.coroutines.flow.Flow

interface FeedRepository<Params> {
    suspend fun upvote(name: Fullname): Result<Unit>
    suspend fun downvote(name: Fullname): Result<Unit>
    suspend fun save(name: Fullname): Result<Unit>
    suspend fun unsave(name: Fullname): Result<Unit>
}

abstract class FeedRepositoryCommon<Params>(
    val votableRepository: VotableRepository,
    val api: RedditAPIService,
) : FeedRepository<Params>, ListingRepository<Params, VotableData>() {

    fun observePost(name: Fullname): Flow<VotableData?> {
        return votableRepository.observePost(name)
    }

    override suspend fun upvote(name: Fullname): Result<Unit> {
        return votableRepository.upvote(name)
    }

    override suspend fun downvote(name: Fullname): Result<Unit> {
        return votableRepository.downvote(name)
    }

    override suspend fun save(name: Fullname): Result<Unit> {
        return votableRepository.save(name)
    }

    override suspend fun unsave(name: Fullname): Result<Unit> {
        return votableRepository.unsave(name)
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