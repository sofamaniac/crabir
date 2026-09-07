/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.Cache
import com.sofamaniac.crabir.domain.repository.ListingRepository
import com.sofamaniac.crabir.domain.repository.ListingSource
import com.sofamaniac.crabir.domain.repository.VotableRepository

interface FeedRepository<Params, T : VotableData> {
    val votableRepository: VotableRepository<T>
    suspend fun upvote(name: Fullname): Result<Unit> {
        return votableRepository.upvote(name)
    }

    suspend fun downvote(name: Fullname): Result<Unit> {
        return votableRepository.downvote(name)
    }

    suspend fun save(name: Fullname): Result<Unit> {
        return votableRepository.save(name)
    }

    suspend fun unsave(name: Fullname): Result<Unit> {
        return votableRepository.unsave(name)
    }
}

abstract class FeedRepositoryCommon<Params, T : VotableData> :
    FeedRepository<Params, T>, ListingRepository<Params, T>() {
    override val cache: Cache<T> get() = votableRepository
}

abstract class PostFeedRepository<Params> :
    FeedRepositoryCommon<Params, PostData>() {
    override fun thingToData(thing: Thing): PostData? {
        return when (thing) {
            is Thing.Post -> {
                PostDataMapper.map(thing.data)
            }

            else -> {
                Log.e("PostFeedRepository", "Expect Post got: $thing")
                null
            }
        }
    }
}

abstract class CommentFeedRepository<Params> :
    FeedRepositoryCommon<Params, CommentType>() {
    override fun thingToData(thing: Thing): CommentType? {
        return when (thing) {
            is Thing.Comment -> {
                CommentType.Comment(CommentDataMapper.map(thing.data))
            }

            else -> {
                throw IllegalArgumentException("Unreachable code")
            }
        }
    }
}

abstract class MixedFeedRepository<Params> :
    FeedRepositoryCommon<Params, VotableData>() {
    override fun thingToData(thing: Thing): VotableData? {
        return when (thing) {
            is Thing.Post -> {
                PostDataMapper.map(thing.data)
            }

            is Thing.Comment -> {
                val data = CommentDataMapper.map(thing.data)
                CommentType.Comment(data)
            }

            else -> {
                throw IllegalArgumentException("Unreachable code")
            }
        }
    }
}

data class FeedParams(val sort: Sort, val timeframe: Timeframe?)

typealias FeedSource<Params, T> = ListingSource<Params, T>
