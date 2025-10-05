package com.soulware.tcompro.shared.data.policies

sealed class OrderDetailType {
    data class Payment(val method: PaymentMethod) : OrderDetailType()
    data class Pickup(val method: PickupMethod) : OrderDetailType()
}