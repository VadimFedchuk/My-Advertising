package com.vadimfedchuk.myadvertising.remote

import java.io.IOException
import okhttp3.ResponseBody
import okhttp3.Interceptor
import okhttp3.Response


class LogJsonInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val rawJson = response.body()?.string()

        return response.newBuilder().body(ResponseBody.create(response.body()?.contentType(), rawJson)).build()
    }
}