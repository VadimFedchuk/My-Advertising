package com.vadimfedchuk.myadvertising.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vadimfedchuk.myadvertising.remote.ChatService
import com.vadimfedchuk.myadvertising.remote.RemoteService
import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.request.FirebaseTokenRequest
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import com.vadimfedchuk.myadvertising.remote.response.*
import com.vadimfedchuk.myadvertising.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdvertisingRepository @Inject constructor(
    private val remoteService: RemoteService,
    private val chatService: ChatService
) : Repository {

    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    override fun requestRegistration(body: RegistrationRequest): LiveData<RegistrationResponse> {
        val mutableData = MutableLiveData<RegistrationResponse>()

        val disposable = remoteService.requestRegistration(body)
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
        val disposable = remoteService.confirmCode(url, token, code)
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
        val disposable = remoteService.login(phone, password)
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
        val disposable = remoteService.recoveryPassword(phone)
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
        val disposable = remoteService.getAllMarkers("Bearer $token")
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
        val disposable = remoteService.createOrder(body, "Bearer $token")
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
        val disposable = remoteService.getHistoryOrders("Bearer $token")
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
        val disposable = remoteService.cancelOrder(order_id, "Bearer $token")
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
        val disposable = remoteService.addUserAvatar("Bearer $token", avatar)
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
        val disposable = remoteService.updateUserInfo(name, password, password,"Bearer $token")
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
        val disposable = remoteService.sendFirebaseToken("Bearer $token", body)
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

    fun sendMessage(message: SendMessageResponse) =
        sendRequest(chatService.sendMessage(
            message.message,
            message.name,
            message.recipient,
            message.content,
            message.token
        ))

    fun getMessages(token: String) =
        sendRequest(chatService.getMessagesByToken(token))

    private fun <T> sendRequest(call: Call<T>?): MutableLiveData<T>? {
        val data = MutableLiveData<T>()
        call?.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>?, t: Throwable?) {

                data.value = null
            }

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                if (response?.code()==200) {
                    data.value = response.body()!!
                } else {
                    data.value = null
                }
            }
        })
        return data
    }
}