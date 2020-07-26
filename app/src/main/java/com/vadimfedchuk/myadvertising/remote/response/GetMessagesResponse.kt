package com.vadimfedchuk.myadvertising.remote.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class GetMessagesResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("messages")
    val messages: List<ChatMessage>
)

data class ChatMessage(
    @SerializedName("message_id")
    val message_id: Int,

    @SerializedName("sender")
    val sender: String,

    @SerializedName("sender_id")
    val sender_id: Int,

    @SerializedName("recipient")
    val recipient: String,

    @SerializedName("recipient_id")
    val recipient_id: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("dt")
    val dt: Date
)
