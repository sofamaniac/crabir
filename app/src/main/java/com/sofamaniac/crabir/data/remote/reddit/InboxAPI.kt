package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.Fullname
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InboxAPI {
    @GET("/message/inbox.json")
    suspend fun inbox(
        @Query("after") after: Fullname? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<Thing.Listing<Thing>>
}