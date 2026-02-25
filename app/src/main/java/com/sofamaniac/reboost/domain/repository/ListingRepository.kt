package com.sofamaniac.reboost.domain.repository

import android.util.Log
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.model.PagedResponse
import retrofit2.Response

abstract class ListingRepository {
    private var _seenThings: Set<String> = emptySet()

    fun refresh() {
        _seenThings = emptySet()
    }

    abstract fun onResponseSuccess(things: List<Thing>)

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