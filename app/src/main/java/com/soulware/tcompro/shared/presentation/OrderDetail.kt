package com.soulware.tcompro.shared.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import com.soulware.tcompro.shared.data.policies.OrderDetailType
import com.soulware.tcompro.shared.data.policies.PaymentMethod
import com.soulware.tcompro.shared.data.policies.PickupMethod

@Composable
fun OrderDetail(detail: OrderDetailType){
    val (icon, text) = when (detail) {
        is OrderDetailType.Payment -> when (detail.method) {
            PaymentMethod.CASH -> Icons.Default.Payments to "Cash Payment"
            PaymentMethod.ON_CREDIT -> Icons.Default.ThumbUp to "On Credit"
            PaymentMethod.VIRTUAL -> Icons.Default.PhoneIphone to "Virtual Payment"
        }

        is OrderDetailType.Pickup -> when (detail.method) {
            PickupMethod.DELIVERY -> Icons.Default.Moped to "Home Delivery"
            PickupMethod.SHOP_PICK_UP -> Icons.Default.Storefront to "Shop Pickup"
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically,
    )
    {
        Icon(
            imageVector = icon,
            contentDescription = text

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun OrderDetailPreview(){
    TcomproTheme {
        OrderDetail(detail = OrderDetailType.Payment(PaymentMethod.CASH))
    }
}