package com.vadimfedchuk.myadvertising.remote.response


import com.google.gson.annotations.SerializedName


data class CreateOrderResponse(

	@field:SerializedName("id")
	val id: Id? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("redirect")
	val url: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("messages")
	val messagesError: MessagesError? = null
)