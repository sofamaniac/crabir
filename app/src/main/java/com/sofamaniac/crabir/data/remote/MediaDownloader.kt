package com.sofamaniac.crabir.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MediaDownloader {
    @Streaming
    @GET
    suspend fun download(@Url url: String): Result<ResponseBody>
}
