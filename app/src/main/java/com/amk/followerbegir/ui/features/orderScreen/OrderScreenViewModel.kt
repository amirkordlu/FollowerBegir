package com.amk.followerbegir.ui.features.orderScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.followerbegir.model.data.OrderStatusResponse
import com.amk.followerbegir.model.repository.orderStatusRepository.OrderStatusRepository
import com.amk.followerbegir.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class OrderScreenViewModel(
    private val orderRepository: OrderStatusRepository
) : ViewModel() {

    val ordersStatusMap = mutableStateOf<Map<String, OrderStatusResponse>>(emptyMap())

    fun getOrdersStatus(orders: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val data = orderRepository.getOrdersStatus(orders)
            ordersStatusMap.value = data
        }
    }
}