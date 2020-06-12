package com.vadimfedchuk.myadvertising.remote.request

import com.google.gson.annotations.SerializedName

data class FirebaseTokenRequest(
    @field: SerializedName("token") val token: String
)