package com.sofamaniac.crabir.domain.repository

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.local.dao.InboxDao
import com.sofamaniac.crabir.data.remote.dto.MessageDTOMapper
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.comment.CommentMessageMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.koin.core.annotation.ViewModelScope


enum class InboxFeed {
    All,
    Unread,
    Sent,
    Mentions
}

class MessageNotFoundException : Exception()

class InboxCache : Cache<Message> {
    private val cache = mutableMapOf<Fullname, Message>()
    override fun insert(thing: Message) {
        cache[thing.name] = thing
    }

    override fun insert(things: List<Message>) {
        things.forEach { insert(it) }
    }

    override fun update(thing: Message) {
        cache[thing.name] = thing
    }

    override fun get(name: Fullname): Flow<Message?> {
        return flowOf(cache[name])
    }

    override suspend fun getAsync(name: Fullname): Message? {
        return cache[name]
    }

    override suspend fun delete(name: Fullname) {
        cache.remove(name)
    }

    override fun clear() {
        cache.clear()
    }
}

abstract class InboxRepository(override val cache: InboxCache = InboxCache()) :
    ListingRepository<InboxFeed, Message>() {
    abstract fun get(name: Fullname): Flow<Message?>
    abstract suspend fun readAll(): Result<Unit>

    abstract suspend fun read(name: Fullname): Result<Unit>

    abstract suspend fun unread(name: Fullname): Result<Unit>
    abstract suspend fun delete(name: Fullname): Result<Unit>
}

@ViewModelScope
class InboxRepositoryImpl(private val inbox: RedditAPIService, private val inboxDao: InboxDao) :
    InboxRepository() {

    override suspend fun onResponseSuccess(things: List<Thing>) {
        super.onResponseSuccess(things)
        val data = things.mapNotNull { thing -> thingToData(thing) }
        inboxDao.insert(data)
    }

    override fun thingToData(thing: Thing): Message? {
        return when (thing) {
            is Thing.Message -> MessageDTOMapper.map(thing.data)
            is Thing.Comment -> CommentMessageMapper.map(thing.data)
            else -> null
        }
    }

    override suspend fun getThings(
        after: Fullname,
        params: InboxFeed,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        return makeRequest {
            when (params) {
                InboxFeed.All -> inbox.inbox(after = after)
                InboxFeed.Unread -> inbox.unread(after = after)
                InboxFeed.Sent -> inbox.sent(after = after)
                InboxFeed.Mentions -> inbox.mentions(after = after)
            }
        }
    }

    override fun get(name: Fullname): Flow<Message?> {
        return inboxDao.get(name)
    }

    override suspend fun readAll(): Result<Unit> {
        return inbox.readAll().onSuccess {
            withContext(Dispatchers.IO) {
                inboxDao.markAllRead()
            }
        }
    }

    override suspend fun read(name: Fullname): Result<Unit> {
        val res = inbox.markRead(name.name)
        if (res.isFailure) return res
        val message =
            inboxDao.getValue(name) ?: return Result.failure(MessageNotFoundException())
        withContext(Dispatchers.IO) {
            inboxDao.insert(message.copy(new = false))
        }
        return Result.success(Unit)
    }

    override suspend fun unread(name: Fullname): Result<Unit> {
        val res = inbox.markUnread(name.name)
        if (res.isFailure) return res
        val message =
            inboxDao.getValue(name) ?: return Result.failure(MessageNotFoundException())
        withContext(Dispatchers.IO) {
            inboxDao.update(message.copy(new = true))
        }
        return Result.success(Unit)
    }

    override suspend fun delete(name: Fullname): Result<Unit> {
        return inbox.delete(name.name).onSuccess {
            withContext(Dispatchers.IO) {
                inboxDao.delete(name)
            }
        }
    }
}
