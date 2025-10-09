package com.soulware.tcompro.features.shop.presentation

import androidx.annotation.StringRes
import com.soulware.tcompro.core.ITabRoute
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.soulware.tcompro.R
import com.soulware.tcompro.core.TwoTabScreen

sealed class ShopInnerTabRoute(
    override val route: String,
    @get:StringRes override val labelResId: Int
) : ITabRoute {
    object TrustedCustomers : ShopInnerTabRoute("shop_trusted_customers_tab", R.string.label_trusted_customers)

    object Staff : ShopInnerTabRoute("shop_staff_tab", R.string.label_staff)
}



@Composable
fun ShopScreen() {
    val shopTabs = listOf(
        ShopInnerTabRoute.TrustedCustomers,
        ShopInnerTabRoute.Staff
    )

    TwoTabScreen(
        tabs = shopTabs,
        content1 = { Text(stringResource(R.string.placeholder_trusted_customers)) },
        content2 = { Text(stringResource(R.string.placeholder_staff)) }
    )
}