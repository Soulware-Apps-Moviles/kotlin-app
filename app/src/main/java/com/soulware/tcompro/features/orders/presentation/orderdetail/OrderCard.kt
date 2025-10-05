package com.soulware.tcompro.features.orders.presentation.orderdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import com.soulware.tcompro.shared.data.policies.OrderDetailType
import com.soulware.tcompro.shared.presentation.OrderDetail
import com.soulware.tcompro.shared.presentation.ProfileDetail

@Composable
fun OrderCard(viewModel: OrderCardViewModel = viewModel()){
    val information by viewModel.information.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileDetail(customerType = information.customerType, information = information.profileInformation)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFDD6529))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ){
                    Text(
                        text = "${information.distanceMeters} meters",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Order type",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDD6529)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
               Column {
                   Text(text = "Pick-up method", fontSize = 12.sp)
                   Text(text = "Payment method", fontSize = 12.sp)
               }
                Column {
                    OrderDetail(detail = OrderDetailType.Pickup(information.pickupMethod))
                    OrderDetail(detail = OrderDetailType.Payment(information.paymentMethod))
                }
            }
            ProductList() // logica
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OrderCardPreview(){
    TcomproTheme {
        OrderCard(

        )
    }
}