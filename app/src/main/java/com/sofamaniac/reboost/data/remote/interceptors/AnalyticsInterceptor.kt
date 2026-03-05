package com.sofamaniac.reboost.data.remote.interceptors

import com.google.firebase.perf.FirebasePerformance
import okhttp3.Interceptor
import okhttp3.Response

class AnalyticsInterceptor : Interceptor {
    private val perf = FirebasePerformance.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val apiTrace = perf.newTrace("api_call")
        apiTrace.start()
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            apiTrace.incrementMetric("success", 1)
        } else {
            apiTrace.incrementMetric("failure", 1)
        }
        apiTrace.stop()
        return response
    }
}