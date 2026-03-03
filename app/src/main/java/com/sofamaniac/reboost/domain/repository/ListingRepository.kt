package com.sofamaniac.reboost.domain.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.model.PagedResponse
import retrofit2.Response

interface DataInterface {
    val id: String
}

abstract class ListingRepository<Params, Data : DataInterface> {
    private var _seenThings: Set<String> = emptySet()
    var cache = mutableMapOf<String, Data>()
        private set

    fun refresh() {
        _seenThings = emptySet()
    }

    open fun onResponseSuccess(things: List<Thing>) {
        val data = things.mapNotNull { thing -> thingToData(thing) }
        cache.putAll(data.associateBy { it.id })
    }

    abstract fun thingToData(thing: Thing): Data?

    abstract suspend fun getThings(after: String, params: Params): PagedResponse<String>


    protected suspend fun <T : Thing> makeRequest(
        request: suspend () -> Response<Thing.Listing<T>>
    ): PagedResponse<String> {
        val response = request()
        if (response.isSuccessful) {
            Log.d("makeRequest", "code ${response.code()}")
            val listing = response.body()
            listing?.let {
                val things = listing.data.children
                    .filter { thing -> !_seenThings.contains(thing.id) }
                things.forEach { data ->
                    _seenThings += data.id
                }
                onResponseSuccess(things)
                val thingIds = things.map { thing ->
                    thing.id
                }
                return PagedResponse(
                    data = thingIds,
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

class ListingSource<Params, Data : DataInterface>(
    private val repository: ListingRepository<Params, Data>,
    private val params: Params,
) : PagingSource<String, Data>() {


    override fun getRefreshKey(state: PagingState<String, Data>): String {
        return ""
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Data> {
        val page = if (params.key != null) {
            getThings(params.key!!)
        } else {
            PagedResponse()
        }
        val data = page.data.mapNotNull { id ->
            repository.cache[id]
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = page.after,
            data = data
        )
    }

    private suspend fun getThings(after: String): PagedResponse<String> {
        return repository.getThings(after, params)
    }
}
