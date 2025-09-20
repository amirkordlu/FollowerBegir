package com.amk.follower.ui.features.detailScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.follower.model.data.ServiceItemsResponse
import com.amk.follower.model.repository.addOrderRepostiory.AddOrderServiceRepository
import com.amk.follower.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.follower.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val serviceRepository: ServiceItemsRepository,
    private val orderRepository: AddOrderServiceRepository
) : ViewModel() {

    val itemDetail = mutableStateOf<ServiceItemsResponse?>(null)
    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf(false)
    val orderMessage = mutableStateOf<String?>(null)
    val orderId = mutableStateOf<Int?>(null)
    val isOrderSaved = mutableStateOf(false)
    private var loadedServiceId: String? = null

    fun loadServiceDetail(serviceId: String) {
        if (loadedServiceId == serviceId && itemDetail.value != null) return
        viewModelScope.launch(coroutineExceptionHandler) {
            isLoading.value = true
            isError.value = false
            try {
                val allItems = serviceRepository.getAllItemsList()
                itemDetail.value = allItems.find { it.service == serviceId }
                loadedServiceId = serviceId
            } catch (_: Exception) {
                isError.value = true
            } finally {
                isLoading.value = false
            }
        }
    }


    fun addOrderService(serviceId: Int, link: String, quantity: Int) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isLoading.value = true
            isError.value = false
            orderMessage.value = null
            try {
                val result = orderRepository.addOrderService(serviceId, link, quantity, 0)
                if (result.status == "success") {
                    orderMessage.value = "✅ سفارش با موفقیت ثبت شد"
                    orderId.value = result.order
                    isOrderSaved.value = false
                } else {
                    orderMessage.value = "❌ ثبت سفارش ناموفق بود"
                    isError.value = true
                }
            } catch (_: Exception) {
                orderMessage.value = "❌ خطا در ثبت سفارش"
                isError.value = true
            } finally {
                isLoading.value = false
                delay(5000)
                orderMessage.value = null
            }
        }
    }
}
