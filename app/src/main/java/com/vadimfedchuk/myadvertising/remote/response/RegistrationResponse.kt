package com.vadimfedchuk.myadvertising.remote.response


import com.google.gson.annotations.SerializedName

data class RegistrationResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("user")
	val userInfo: User? = null
)