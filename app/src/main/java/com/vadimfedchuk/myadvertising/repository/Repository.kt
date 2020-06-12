package com.vadimfedchuk.myadvertising.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import com.vadimfedchuk.myadvertising.remote.response.*
import com.vadimfedchuk.myadvertising.utils.Resource
import okhttp3.MultipartBody

interface Repository {

  fun requestRegistration(body: RegistrationRequest): LiveData<RegistrationResponse>

  fun requestConfirmCode(url:String, token: String, code: String): LiveData<RegistrationResponse>

  fun requestLogin(phone: String, password: String): LiveData<LoginResponse>

  fun updateUserInfo(name: String, password: String, token: String): LiveData<LoginResponse>

  fun recoveryPassword(phone: String): LiveData<RegistrationResponse>

  fun getAllMarkers(token: String): MutableLiveData<Resource<GetAllMarkersResponse>>

  fun createOrder(token: String, body: CreateOrderRequest): LiveData<CreateOrderResponse>

  fun cancelOrder(token: String, order_id: Int): LiveData<CancelOrderResponse>

  fun getHistoryOrders(token: String): LiveData<HistoryOrdersResponse>

  fun addUserAvatar(token: String, avatar: MultipartBody.Part): LiveData<AddUserAvatarResponse>


}
