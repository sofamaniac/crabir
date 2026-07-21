package com.sofamaniac.crabir.domain.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface DataInterface {
    val id: String
    val name: Fullname
}

abstract class ListingRepository<Params, Data : DataInterface> {
    private var _seenThings: Set<Fullname> = emptySet()
    var cache = mutableMapOf<Fullname, Data>()
        private set

    open fun refresh() {
        _seenThings = emptySet()
    }

    open suspend fun onResponseSuccess(things: List<Thing>) {
        val data = things.mapNotNull { thing -> thingToData(thing) }
        cache.putAll(data.associateBy { it.name })
    }

    abstract fun thingToData(thing: Thing): Data?

    abstract suspend fun getThings(
        after: Fullname,
        params: Params,
    ): PagingSource.LoadResult<Fullname, Fullname>


    protected suspend fun <T : Thing> makeRequest(
        request: suspend () -> Response<Thing.Listing<T>>,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        val response = try {
            request()
        } catch (e: Exception) {
            Log.e("makeRequest", "Error making request", e)
            return PagingSource.LoadResult.Error(e)
        }
        if (response.isSuccessful) {
            val listing = response.body()
            if (listing != null) {
                val things = listing.data.children
                    .filter { thing -> !_seenThings.contains(thing.name) }
                things.forEach { data ->
                    _seenThings += data.name
                }
                withContext(Dispatchers.IO) {
                    onResponseSuccess(things)
                }
                val thingsName = things.map { thing ->
                    thing.name
                }
                return PagingSource.LoadResult.Page(
                    data = thingsName,
                    nextKey = listing.data.after,
                    prevKey = null
                )
            } else {
                return PagingSource.LoadResult.Page(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = null
                )
            }
        } else {
            Log.e("makeRequest", "Error making request : ${response.errorBody()}")
            return PagingSource.LoadResult.Error(Exception("Error making request ${response.errorBody()}"))
        }
    }
}

class ListingSource<Params, Data : DataInterface>(
    private val repository: ListingRepository<Params, Data>,
    private val params: Params,
) : PagingSource<Fullname, Data>() {


    override fun getRefreshKey(state: PagingState<Fullname, Data>): Fullname {
        return Fullname("")
    }

    override suspend fun load(params: LoadParams<Fullname>): LoadResult<Fullname, Data> {
        if (params.key == null) {
            return LoadResult.Page(prevKey = null, nextKey = null, data = emptyList())
        }
        try {
            val page = getThings(params.key!!)
            if (page is LoadResult.Error) {
                return LoadResult.Error(page.throwable)
            }
            if (page !is LoadResult.Page) {
                return LoadResult.Error(Exception("Invalid load result"))
            }
            val data = page.data.mapNotNull { id ->
                repository.cache[id]
            }
            return LoadResult.Page(
                prevKey = null,
                nextKey = page.nextKey,
                data = data
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

    private suspend fun getThings(after: Fullname): PagingSource.LoadResult<Fullname, Fullname> {
        return repository.getThings(after, params)
    }
}
