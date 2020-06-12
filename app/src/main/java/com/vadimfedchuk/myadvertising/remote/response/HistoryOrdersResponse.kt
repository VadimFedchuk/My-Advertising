package com.vadimfedchuk.myadvertising.remote.response


import com.google.gson.annotations.SerializedName


data class HistoryOrdersResponse(

	@field:SerializedName("orders")
	val orders: List<OrdersItem>? = null
)