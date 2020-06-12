package com.vadimfedchuk.myadvertising.remote

import android.content.Context
import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.request.FirebaseTokenRequest
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val remoteService: RemoteService) {

  fun requestRegistration(body: RegistrationRequest) =
      remoteService.requestRegistration(body)

  fun confirmCode(url:String, token: String, code: String) =
      remoteService.confirmCode(url, token, code)

  fun requestLogin(phone: String, password: String) =
      remoteService.login(phone, password)

    fun recoveryPassword(phone: String) =
        remoteService.recoveryPassword(phone)

    fun getAllMarkers(token: String) =
        remoteService.getAllMarkers("Bearer $token")

    fun createOrder(token: String, body: CreateOrderRequest) =
        remoteService.createOrder(body, "Bearer $token")

    fun cancelOrder(token: String, id: Int) =
        remoteService.cancelOrder(id, "Bearer $token")

    fun getHistoryOrders(token: String) =
        remoteService.getHistoryOrders("Bearer $token")

    fun addUserAvatar(token: String, avatar: MultipartBody.Part) =
        remoteService.addUserAvatar("Bearer $token", avatar)

    fun updateUserInfo(name: String, password: String, token: String) =
        remoteService.updateUserInfo(name, password, password, "Bearer $token")

    fun sendFirebaseToken(token: String, body:FirebaseTokenRequest) =
        remoteService.sendFirebaseToken("Bearer $token", body)

}

