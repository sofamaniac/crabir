package com.sofamaniac.crabir.domain.repository

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MessageData
import org.koin.core.annotation.ViewModelScope


enum class InboxFeed {
    Inbox,
    Unread,
    Sent,
    Mentions
}

abstract class InboxRepository : ListingRepository<InboxFeed, MessageData>() {

    abstract suspend fun readAll(): Result<Unit>

    abstract suspend fun read(name: Fullname): Result<Unit>

    abstract suspend fun unread(name: Fullname): Result<Unit>
}

@ViewModelScope
class InboxRepositoryImpl(private val inbox: RedditAPIService) :
    InboxRepository() {
    override fun thingToData(thing: Thing): MessageData? {
        return when (thing) {
            is Thing.Message -> MessageData.Message(thing.data)
            is Thing.Comment -> MessageData.Comment(thing.data)
            else -> null
        }
    }

    override suspend fun getThings(
        after: Fullname,
        params: InboxFeed,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        return makeRequest {
            when (params) {
                InboxFeed.Inbox -> inbox.inbox(after = after)
                InboxFeed.Unread -> inbox.unread(after = after)
                InboxFeed.Sent -> inbox.sent(after = after)
                InboxFeed.Mentions -> inbox.mentions(after = after)
            }
        }
    }

    override suspend fun readAll(): Result<Unit> {
        try {
            inbox.readAll()
            cache.replaceAll { _, value ->
                when (value) {
                    is MessageData.Comment -> MessageData.Comment(value.comment.copy(new = false))
                    is MessageData.Message -> MessageData.Message(value.message.copy(new = false))
                }
            }
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun read(name: Fullname): Result<Unit> {
        try {
            inbox.markRead(name.name)
            val newMessage = when (val message = cache[name]) {
                is MessageData.Comment -> MessageData.Comment(message.comment.copy(new = true))
                is MessageData.Message -> MessageData.Message(message.message.copy(new = true))
                null -> null
            }
            if (newMessage != null) {
                cache[name] = newMessage
            }
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun unread(name: Fullname): Result<Unit> {
        try {
            inbox.markUnread(name.name)
            val newMessage = when (val message = cache[name]) {
                is MessageData.Comment -> MessageData.Comment(message.comment.copy(new = false))
                is MessageData.Message -> MessageData.Message(message.message.copy(new = false))
                null -> null
            }
            if (newMessage != null) {
                cache[name] = newMessage
            }
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}