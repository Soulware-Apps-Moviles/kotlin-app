package com.soulware.tcompro.shared.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import com.soulware.tcompro.shared.data.profiles.CustomerType
import com.soulware.tcompro.shared.data.profiles.ProfileInformation

@Composable
fun ProfileDetail(customerType: CustomerType, information: ProfileInformation){
    val type = when (customerType){
        CustomerType.NEW -> "New customer!"
        CustomerType.REGULAR -> "Regular customer"
        CustomerType.TRUSTED -> "Trusted customer"
    }

    Row(
        modifier = Modifier
            .height(40.dp)
    )
    {
        Column(
            modifier = Modifier
            .width(40.dp)
            .height(40.dp)
        ){
            AsyncImage(
                model = information.image,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Column {
            Row (modifier = Modifier.height(18.dp)){
                Text(text = type, color = Color(0xFFDD6529), fontSize = 10.sp)
            }
            Row {
                Text(text = information.firstName + " " + information.lastName, fontSize = 16.sp)
            }
        }
    }
}

@Preview
@Composable
fun ProfileDetailPreview(){
    TcomproTheme {
        ProfileDetail(
            customerType = CustomerType.REGULAR,
            information = ProfileInformation("Carlos", "Ochoa", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.facebook.com%2FPlaceHolderPGH%2Fphotos%2F%3Flocale%3Dko_KR&psig=AOvVaw2MmmTTfEUIYDbOutFu9s0W&ust=1759709893510000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCLCBuazki5ADFQAAAAAdAAAAABAJ")
        )
    }
}