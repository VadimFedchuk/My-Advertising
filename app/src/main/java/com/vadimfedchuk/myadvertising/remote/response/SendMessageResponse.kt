package com.vadimfedchuk.myadvertising.remote.response

import com.google.gson.annotations.SerializedName

data class SendMessageResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("recipient")
    val recipient: Int,

    @SerializedName("content")
    val content: String,

    @SerializedName("token")
    val token: String,

    @SerializedName("status")
    val status: String? = null
)