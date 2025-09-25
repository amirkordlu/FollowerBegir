package com.amk.sakoo.ui.features.orderScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.sakoo.model.data.OrderStatusResponse
import com.amk.sakoo.model.repository.orderStatusRepository.OrderStatusRepository
import kotlinx.coroutines.launch

class OrderScreenViewModel(
    private val orderRepository: OrderStatusRepository
) : ViewModel() {

    val ordersStatusMap = mutableStateOf<Map<String, OrderStatusResponse>>(emptyMap())
    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf(false)

    fun getOrdersStatus(orders: String) {
        viewModelScope.launch {
            isLoading.value = true
            isError.value = false
            try {
                val data = orderRepository.getOrdersStatus(orders)
                ordersStatusMap.value = data
            } catch (e: Exception) {
                isError.value = true
                ordersStatusMap.value = emptyMap()
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}