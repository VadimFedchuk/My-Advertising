package com.vadimfedchuk.myadvertising.di

import com.vadimfedchuk.myadvertising.ui.fragment.login.registration.RegistrationViewModel
import com.vadimfedchuk.myadvertising.ui.fragment.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RemoteModule::class])
@Singleton interface AppComponent {

    fun inject(viewModel: RegistrationViewModel)
    fun inject(viewModel: MainViewModel)

}