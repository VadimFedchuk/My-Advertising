package com.vadimfedchuk.myadvertising.remote.request

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @field: SerializedName("marker_id") val idMarker: String,
    @field: SerializedName("start_date") val dateStart: String,
    @field: SerializedName("end_date") val dateEnd: String
)