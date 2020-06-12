package com.vadimfedchuk.myadvertising.remote.response

import com.google.gson.annotations.SerializedName

data class CancelOrderResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null

)