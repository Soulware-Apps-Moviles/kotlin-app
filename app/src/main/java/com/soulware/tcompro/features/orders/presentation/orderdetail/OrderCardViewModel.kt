package com.soulware.tcompro.features.orders.presentation.orderdetail

import androidx.lifecycle.ViewModel
import com.soulware.tcompro.features.orders.data.OrderInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrderCardViewModel @Inject constructor() : ViewModel(){
    private val _information = MutableStateFlow(OrderInformation())
    val information: StateFlow<OrderInformation> = _information.asStateFlow()



}

