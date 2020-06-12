package com.vadimfedchuk.myadvertising.ui.fragment.login.registration

import android.util.Log
import androidx.lifecycle.*
import com.vadimfedchuk.myadvertising.di.AdvertisingApplication
import com.vadimfedchuk.myadvertising.remote.RemoteContract
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import com.vadimfedchuk.myadvertising.remote.response.LoginResponse
import com.vadimfedchuk.myadvertising.remote.response.RegistrationResponse
import com.vadimfedchuk.myadvertising.repository.AdvertisingRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RegistrationViewModel  : ViewModel(), LifecycleObserver {

    @Inject
    lateinit var repository: AdvertisingRepository

    private val compositeDisposable = CompositeDisposable()
    private var registrationData: LiveData<RegistrationResponse>? = null
    private var confirmCodeData: LiveData<RegistrationResponse>? = null
    private var recoveryPasswordData: LiveData<RegistrationResponse>? = null
    private var loginData: LiveData<LoginResponse>? = null
    private var subscribeData:LiveData<RegistrationResponse>? = null

    init {
        initializeDagger()
    }

    fun requestLogin(phone: String, password: String): LiveData<LoginResponse>? {
        loginData = null
        loginData = MutableLiveData<LoginResponse>()
        loginData = repository.requestLogin(phone, password)
        return loginData
    }

    fun registrationRequest(body: RegistrationRequest): LiveData<RegistrationResponse>? {
        registrationData = null
        registrationData = MutableLiveData<RegistrationResponse>()
        registrationData = repository.requestRegistration(body)
        return registrationData
    }

    fun requestCode(token: String, code: String): LiveData<RegistrationResponse>? {
        confirmCodeData = null
        confirmCodeData = MutableLiveData<RegistrationResponse>()
        confirmCodeData = repository.requestConfirmCode(RemoteContract.BASE_URL + RemoteContract.CONFIRM_CODE_PART, token, code)
        return confirmCodeData
    }

    fun recoveryPassword(token: String, code: String): LiveData<RegistrationResponse>? {
        confirmCodeData = null
        confirmCodeData = MutableLiveData<RegistrationResponse>()
        confirmCodeData = repository.requestConfirmCode(RemoteContract.BASE_URL + RemoteContract.CONFIRM_RECOVERY_CODE_PART, token, code)
        return confirmCodeData
    }

    fun recoveryPassword(phone: String): LiveData<RegistrationResponse>? {
        recoveryPasswordData = null
        recoveryPasswordData = MutableLiveData<RegistrationResponse>()
        recoveryPasswordData = repository.recoveryPassword(phone)
        return recoveryPasswordData
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unSubscribeViewModel() {
        for (disposable in repository.allCompositeDisposable) {
            compositeDisposable.addAll(disposable)
        }
        compositeDisposable.clear()
    }

    fun sendFirebaseToken(token: String, firebaseToken:String):LiveData<RegistrationResponse>? {
        subscribeData = null
        subscribeData = MutableLiveData<RegistrationResponse>()
        subscribeData = repository.sendFirebaseToken(token, firebaseToken)
        Log.i("TEST", "3 " + subscribeData.toString())
        return subscribeData
    }

    override fun onCleared() {
        unSubscribeViewModel()
        super.onCleared()
    }


    private fun initializeDagger() = AdvertisingApplication.appComponent.inject(this)
}