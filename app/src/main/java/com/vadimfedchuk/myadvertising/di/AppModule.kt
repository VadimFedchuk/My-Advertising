package com.vadimfedchuk.myadvertising.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val converterApplication: AdvertisingApplication) {

    @Provides @Singleton fun provideContext(): Context = converterApplication
}