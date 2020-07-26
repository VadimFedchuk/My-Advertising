package com.vadimfedchuk.myadvertising.remote


import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

class ChatServiceProvide {

    companion object {
        var userAgent = System.getProperty("http.agent")
        var retrofit: Retrofit? = null

        fun <S> getApi(serviceClass: Class<S>) = retrofit?.create(serviceClass)!!

        fun create(url: String, userAgent: String? = null, logEnabled: Boolean = true): Companion {
            val httpUrl = HttpUrl.parse(url)
            val UA = userAgent ?: Companion.userAgent

            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger{})
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .connectTimeout(RemoteContract.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(RemoteContract.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .addInterceptor(UserAgentInterceptor(UA!!))
                .addInterceptor(LogJsonInterceptor())
                .build()

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(httpUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))

            if (logEnabled) {
                retrofitBuilder.client(client)
            }

            retrofit = retrofitBuilder.build()

            return this
        }
    }
}