package com.vadimfedchuk.myadvertising.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex

class AdvertisingApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .remoteModule(RemoteModule())
            .build()
    }
}