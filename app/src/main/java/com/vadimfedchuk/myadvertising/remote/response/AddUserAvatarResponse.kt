package com.vadimfedchuk.myadvertising.remote.response

import com.google.gson.annotations.SerializedName

data class AddUserAvatarResponse(
    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)