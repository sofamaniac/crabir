package com.sofamaniac.crabir.data.remote.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

object CountInterceptor : Interceptor {
    var count = 0
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        count += 1
        Log.d("CountInterceptor", "Request count this session: $count")

        return response
    }
}
