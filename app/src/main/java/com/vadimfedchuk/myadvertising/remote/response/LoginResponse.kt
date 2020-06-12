package com.vadimfedchuk.myadvertising.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("user")
    val userInfo: User? = null
)

data class User(
    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("phone")
    val phone: String = "",

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("updated_at")
    val updated_at: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("image")
    val image: String? = null
)