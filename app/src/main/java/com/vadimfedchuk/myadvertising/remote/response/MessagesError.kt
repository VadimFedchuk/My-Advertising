package com.vadimfedchuk.myadvertising.remote.response

import com.google.gson.annotations.SerializedName

data class MessagesError (
    @field:SerializedName("start_date")
    val start_date: String? = null,

    @field:SerializedName("marker_id")
    val marker_id: String? = null,

    @field:SerializedName("end_date")
    val end_date: String? = null
)