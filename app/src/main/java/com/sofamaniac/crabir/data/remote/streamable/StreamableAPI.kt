package com.sofamaniac.crabir.data.remote.streamable

import retrofit2.http.GET
import retrofit2.http.Path

interface StreamableAPI {
    @GET("/videos/{id}")
    suspend fun getVideo(@Path("id") id: String): Result<StreamableAnswer>
}
