package com.vadimfedchuk.myadvertising.remote.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @field: SerializedName("name") val name: String,
    @field: SerializedName("password") val password: String,
    @field: SerializedName("password_confirmation") val password_confirmation: String,
    @field: SerializedName("phone") val phone: String
)