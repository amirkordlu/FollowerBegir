package com.amk.sakoo.ui.features.detailScreen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.sakoo.model.data.ServiceItemsResponse
import com.amk.sakoo.model.repository.addOrderRepostiory.AddOrderServiceRepository
import com.amk.sakoo.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.sakoo.ui.features.profileScreen.AccountViewModel
import com.amk.sakoo.util.coroutineExceptionHandler
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


    fun addOrderService(
        serviceId: Int,
        link: String,
        quantity: Int,
        amount: Int,
        context: Context,
        lifecycleOwner: LifecycleOwner,
        accountViewModel: AccountViewModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
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

                    try {
                        val walletDeducted =
                            accountViewModel.decreaseWalletSuspend(context, lifecycleOwner, amount)
                        if (!walletDeducted) {
                            onError("خطا در کسر مبلغ از کیف پول - موجودی کافی نیست")
                            return@launch
                        }
                    } catch (e: Exception) {
                        onError("خطا در کسر مبلغ از کیف پول: ${e.message}")
                        return@launch
                    }

                    onSuccess()
                } else {
                    orderMessage.value = "❌ ثبت سفارش ناموفق بود"
                    isError.value = true
                    onError("ثبت سفارش ناموفق بود")
                }
            } catch (e: Exception) {
                orderMessage.value = "❌ خطا در ثبت سفارش"
                isError.value = true
                onError("خطا در ثبت سفارش: ${e.message}")
            } finally {
                isLoading.value = false
                delay(5000)
                orderMessage.value = null
            }
        }
    }
}
