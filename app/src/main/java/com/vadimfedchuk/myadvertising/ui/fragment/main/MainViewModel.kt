package com.vadimfedchuk.myadvertising.ui.fragment.main


import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vadimfedchuk.myadvertising.di.AdvertisingApplication
import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.response.*
import com.vadimfedchuk.myadvertising.repository.AdvertisingRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import android.provider.MediaStore
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.loader.content.CursorLoader
import com.vadimfedchuk.myadvertising.utils.Resource


class MainViewModel : ViewModel(), LifecycleObserver {

    @Inject
    lateinit var repository: AdvertisingRepository

    private var markersData = MutableLiveData<Resource<GetAllMarkersResponse>>()
    private var createOrderData: LiveData<CreateOrderResponse>? = null
    private var cancelOrderData: LiveData<CancelOrderResponse>? = null
    private var historyOrdersData: LiveData<HistoryOrdersResponse>? = null
    private var addUserAvatarData: LiveData<AddUserAvatarResponse>? = null
    private var updateUserData: LiveData<LoginResponse>? = null
    private var subscribeData:LiveData<RegistrationResponse>? = null

    init {
        initializeDagger()
    }

    fun getAllMarkers(token: String):MutableLiveData<Resource<GetAllMarkersResponse>> {
        markersData = repository.getAllMarkers(token)
        return markersData
    }

    fun createOrder(token: String, body: CreateOrderRequest):LiveData<CreateOrderResponse>? {
        createOrderData = null
        createOrderData = MutableLiveData<CreateOrderResponse>()
        createOrderData = repository.createOrder(token, body)
        return createOrderData
    }

    fun getHistoryOrders(token: String):LiveData<HistoryOrdersResponse>? {
        historyOrdersData = null
        historyOrdersData = MutableLiveData<HistoryOrdersResponse>()
        historyOrdersData = repository.getHistoryOrders(token)
        return historyOrdersData
    }

    fun cancelOrder(token: String, order_id: Int):LiveData<CancelOrderResponse>? {
        cancelOrderData = null
        cancelOrderData = MutableLiveData<CancelOrderResponse>()
        cancelOrderData = repository.cancelOrder(token, order_id)
        return cancelOrderData
    }

    fun addUserAvatar(token: String, uriFile: String):LiveData<AddUserAvatarResponse>? {
        val file = File(uriFile)
        val filePart = MultipartBody.Part.createFormData(
            "avatar", file.name,
            RequestBody.create(MediaType.parse("image/png"), file)
        )
        addUserAvatarData = null
        addUserAvatarData = MutableLiveData<AddUserAvatarResponse>()
        addUserAvatarData = repository.addUserAvatar(token, filePart)
        return addUserAvatarData
    }

    fun updateUserInfo(name: String, password: String, token: String):LiveData<LoginResponse>? {
        updateUserData = null
        updateUserData = MutableLiveData<LoginResponse>()
        updateUserData = repository.updateUserInfo(name, password, token)
        return updateUserData
    }

    fun sendFirebaseToken(token: String, firebaseToken:String):LiveData<RegistrationResponse>? {
        subscribeData = null
        subscribeData = MutableLiveData<RegistrationResponse>()
        subscribeData = repository.sendFirebaseToken(token, firebaseToken)
        return subscribeData
    }

    private fun initializeDagger() = AdvertisingApplication.appComponent.inject(this)
}