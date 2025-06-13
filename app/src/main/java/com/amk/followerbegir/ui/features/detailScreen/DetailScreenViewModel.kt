package com.amk.followerbegir.ui.features.detailScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.model.repository.ServiceItemsRepository
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val repository: ServiceItemsRepository
) : ViewModel() {

    val itemDetail = mutableStateOf<ServiceItemsResponse?>(null)
    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf(false)

    fun loadServiceDetail(serviceId: String) {
        viewModelScope.launch {
            isLoading.value = true
            isError.value = false
            try {
                val allItems = repository.getAllItemsList()
                itemDetail.value = allItems.find { it.service == serviceId }
            } catch (_: Exception) {
                isError.value = true
            } finally {
                isLoading.value = false
            }
        }
    }
}
