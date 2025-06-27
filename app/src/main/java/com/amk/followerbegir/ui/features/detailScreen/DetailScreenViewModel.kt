package com.amk.followerbegir.ui.features.detailScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.model.repository.AddOrderServiceRepository
import com.amk.followerbegir.model.repository.ServiceItemsRepository
import com.amk.followerbegir.util.coroutineExceptionHandler
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

    fun loadServiceDetail(serviceId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isLoading.value = true
            isError.value = false
            try {
                val allItems = serviceRepository.getAllItemsList()
                itemDetail.value = allItems.find { it.service == serviceId }
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
                val result = orderRepository.addOrderService(serviceId, link, quantity, 1)
                if (result.status == "success") {
                    orderMessage.value = "✅ سفارش با موفقیت ثبت شد"
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
