package com.vadimfedchuk.myadvertising.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vadimfedchuk.myadvertising.remote.RemoteContract
import com.vadimfedchuk.myadvertising.remote.RemoteService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides @Singleton fun provideGson(): Gson =
        GsonBuilder().setLenient().create()

    @Provides @Singleton fun provideOkHttpClient() : OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest: Request

                val headers = request.newBuilder()

                headers.addHeader("Accept", "application/json")

                newRequest = headers.build()

                chain.proceed(newRequest)
            }
            .build()

    @Provides @Singleton fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(RemoteContract.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides @Singleton fun provideRemoteCurrencyService(retrofit: Retrofit): RemoteService =
        retrofit.create(RemoteService::class.java)
}