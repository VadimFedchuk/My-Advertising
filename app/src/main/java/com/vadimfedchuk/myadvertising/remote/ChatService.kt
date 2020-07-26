package com.vadimfedchuk.myadvertising.remote

import com.vadimfedchuk.myadvertising.remote.response.GetMessagesResponse
import com.vadimfedchuk.myadvertising.remote.response.SendMessageResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChatService {
    @FormUrlEncoded
    @POST("sendMessage")
    fun sendMessage(
        @Field("message") message: String,
        @Field("name") name: String,
        @Field("recipient") recipient: Int,
        @Field("content") content: String,
        @Field("fb_token") fb_token: String
    ): Call<SendMessageResponse>

    @FormUrlEncoded
    @POST("getMessagesByFbToken")
    fun getMessagesByToken(
        @Field("fb_token") fb_token: String
    ): Call<GetMessagesResponse>
}