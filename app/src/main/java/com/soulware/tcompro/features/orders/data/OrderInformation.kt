package com.soulware.tcompro.features.orders.data

import com.soulware.tcompro.shared.data.policies.PaymentMethod
import com.soulware.tcompro.shared.data.policies.PickupMethod
import com.soulware.tcompro.shared.data.profiles.CustomerType
import com.soulware.tcompro.shared.data.profiles.ProfileInformation

data class OrderInformation(
    val profileInformation: ProfileInformation = ProfileInformation("John", "Doe", ""),
    val distanceMeters: Int = 100,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val pickupMethod: PickupMethod = PickupMethod.SHOP_PICK_UP,
    val customerType: CustomerType = CustomerType.REGULAR
)
