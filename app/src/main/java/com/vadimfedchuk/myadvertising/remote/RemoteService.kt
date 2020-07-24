package com.vadimfedchuk.myadvertising.remote

import com.vadimfedchuk.myadvertising.remote.request.CreateOrderRequest
import com.vadimfedchuk.myadvertising.remote.request.FirebaseTokenRequest
import com.vadimfedchuk.myadvertising.remote.request.RegistrationRequest
import com.vadimfedchuk.myadvertising.remote.response.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface RemoteService {

    @POST(RemoteContract.REGISTRATION_PART)
    fun requestRegistration(
        @Body body: RegistrationRequest
    ): Single<RegistrationResponse>

    @POST
    fun confirmCode(
        @Url url: String,
        @Query("token") token: String,
        @Query("code") code: String
    ): Single<RegistrationResponse>

    @POST(RemoteContract.LOGIN_PART)
    fun login(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Single<LoginResponse>

    @POST(RemoteContract.RECOVERY_PASSWORD_PART)
    fun recoveryPassword(
        @Query("phone") phone: String
    ): Single<RegistrationResponse>

    @GET(RemoteContract.GET_ALL_MARKERS_PART)
    fun getAllMarkers(
        @Header ("Authorization") token: String
    ): Observable<GetAllMarkersResponse>

    @POST(RemoteContract.CREATE_ORDER_PART)
    fun createOrder(
        @Body body: CreateOrderRequest,
        @Header ("Authorization") token: String
    ): Single<CreateOrderResponse>

    @POST(RemoteContract.CANCEL_ORDER_PART)
    fun cancelOrder(
        @Query("id") order_id: Int,
        @Header ("Authorization") token: String
    ): Single<CancelOrderResponse>

    @GET(RemoteContract.GET_HISTORY_ORDERS_PART)
    fun getHistoryOrders(
        @Header ("Authorization") token: String
    ): Single<HistoryOrdersResponse>

    @Multipart
    @POST(RemoteContract.ADD_AVATAR_USER_PART)
    fun addUserAvatar(
        @Header ("Authorization") token: String,
        @Part avatar: MultipartBody.Part
    ):Single<AddUserAvatarResponse>

    @POST(RemoteContract.UPDATE_INFO_USER_PART)
    fun updateUserInfo(
        @Query("name") name: String,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String,
        @Header ("Authorization") token: String
    ): Single<LoginResponse>

    @POST(RemoteContract.REGISTRATION_SUBSCRIBE)
    fun sendFirebaseToken(
        @Header ("Authorization") token: String,
        @Body body: FirebaseTokenRequest
    ): Single<RegistrationResponse>
}