package com.sofamaniac.crabir.data.remote.reddit

import com.sofamaniac.crabir.data.remote.dto.Thing
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InboxAPI {

    @GET("/message/inbox.json")
    suspend fun inbox(
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Thing.Listing<Thing>>
}