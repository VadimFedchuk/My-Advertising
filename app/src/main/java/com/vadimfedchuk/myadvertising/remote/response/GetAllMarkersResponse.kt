package com.vadimfedchuk.myadvertising.remote.response


import com.google.gson.annotations.SerializedName


data class GetAllMarkersResponse(

	@field:SerializedName("markers")
	val markers: List<MarkersItem>? = null
)