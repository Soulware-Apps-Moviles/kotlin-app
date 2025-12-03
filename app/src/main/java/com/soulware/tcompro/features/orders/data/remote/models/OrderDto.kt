package com.soulware.tcompro.features.orders.data.remote.models

import com.google.gson.annotations.SerializedName

data class OrderDto(
    val customerId: Int,
    val id: Int,
    @SerializedName("orderlines")
    val orderLines: List<OrderLineDto>,
    val paymentMethod: String,
    val pickupMethod: String,
    val shopId: Int,
    val status: String
)