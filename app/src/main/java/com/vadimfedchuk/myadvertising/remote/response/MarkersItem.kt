package com.vadimfedchuk.myadvertising.remote.response


import com.google.gson.annotations.SerializedName

data class MarkersItem(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("price_month")
	val priceMonth: Int? = null,

	@field:SerializedName("latitude")
	val latitude: String = "",

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("type_price")
	val typePrice: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("size_billboard")
	val sizeBillboard: String? = null,

	@field:SerializedName("price_year")
	val priceYear: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("longitude")
	val longitude: String = "",

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)