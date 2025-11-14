package com.soulware.tcompro.features.orders.domain.models

import com.google.gson.annotations.SerializedName

data class Order(
    val id: Int,
    @SerializedName("orderlines")
    val orderLines: List<OrderLine>,
    val customerId: Int,
    val shopId: Int,
    val paymentMethod: String,
    val pickupMethod: String,
    val status: String
)