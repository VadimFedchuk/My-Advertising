package com.vadimfedchuk.myadvertising.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vadimfedchuk.myadvertising.remote.RemoteDataSource
import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.request.FirebaseTokenRequest
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import com.vadimfedchuk.myadvertising.remote.response.*
import com.vadimfedchuk.myadvertising.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvertisingRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    override fun requestRegistration(body: RegistrationRequest): LiveData<RegistrationResponse> {
        val mutableData = MutableLiveData<RegistrationResponse>()

        val disposable = remoteDataSource.requestRegistration(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it

            }, { throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun requestConfirmCode(url:String, token: String, code: String): LiveData<RegistrationResponse> {
        val mutableData = MutableLiveData<RegistrationResponse>()
        val disposable = remoteDataSource.confirmCode(url, token, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it
            }, { throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun requestLogin(phone: String, password: String): LiveData<LoginResponse> {
        val mutableData = MutableLiveData<LoginResponse>()
        val disposable = remoteDataSource.requestLogin(phone, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it

            }, { throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun recoveryPassword(phone: String): LiveData<RegistrationResponse> {
        val mutableData = MutableLiveData<RegistrationResponse>()
        val disposable = remoteDataSource.recoveryPassword(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it
            }, { throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun getAllMarkers(token: String): MutableLiveData<Resource<GetAllMarkersResponse>> {
        val mutableData = MutableLiveData<Resource<GetAllMarkersResponse>>()
        val disposable = remoteDataSource.getAllMarkers(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //.repeatWhen { completed -> completed.delay(5, TimeUnit.MINUTES)  }
            .doOnError { mutableData.postValue(Resource.error("Something Went Wrong", null)) }
            .subscribe({
                mutableData.postValue(Resource.success(it))
            }, {throwable: Throwable? ->
                throwable?.printStackTrace()
                mutableData.postValue(Resource.error("Something Went Wrong", null))
            })
    allCompositeDisposable.add(disposable)
    return mutableData
    }

    private fun showConnectionDialog(error: Throwable?) {

    }


    override fun createOrder(token: String, body: CreateOrderRequest): LiveData<CreateOrderResponse> {
        val mutableData = MutableLiveData<CreateOrderResponse>()
        val disposable = remoteDataSource.createOrder(token, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it
            }, {throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun getHistoryOrders(token: String): LiveData<HistoryOrdersResponse> {
        val mutableData = MutableLiveData<HistoryOrdersResponse>()
        val disposable = remoteDataSource.getHistoryOrders(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it
            }, {throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun cancelOrder(token: String, order_id: Int): LiveData<CancelOrderResponse> {
        val mutableData = MutableLiveData<CancelOrderResponse>()
        val disposable = remoteDataSource.cancelOrder(token, order_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it
            }, {throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun addUserAvatar(token: String, avatar: MultipartBody.Part
    ): LiveData<AddUserAvatarResponse> {
        val mutableData = MutableLiveData<AddUserAvatarResponse>()
        val disposable = remoteDataSource.addUserAvatar(token, avatar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it
            }, {throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    override fun updateUserInfo(
        name: String, password: String, token: String): LiveData<LoginResponse> {
        val mutableData = MutableLiveData<LoginResponse>()
        val disposable = remoteDataSource.updateUserInfo(name, password, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it

            }, { throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }

    fun sendFirebaseToken(
        token: String, firebaseToken: String): LiveData<RegistrationResponse> {
        val body = FirebaseTokenRequest(firebaseToken)
        val mutableData = MutableLiveData<RegistrationResponse>()
        val disposable = remoteDataSource.sendFirebaseToken(token, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { _error -> showConnectionDialog(_error) }
            .subscribe({
                mutableData.value = it

            }, { throwable: Throwable? ->
                throwable?.printStackTrace()
            })
        allCompositeDisposable.add(disposable)
        return mutableData
    }
}