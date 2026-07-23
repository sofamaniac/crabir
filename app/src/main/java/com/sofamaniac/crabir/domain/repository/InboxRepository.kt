package com.sofamaniac.crabir.domain.repository

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MessageData
import org.koin.core.annotation.ViewModelScope

abstract class InboxRepository : ListingRepository<Unit, MessageData>()

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
        params: Unit,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        return makeRequest { inbox.inbox() }
    }
}