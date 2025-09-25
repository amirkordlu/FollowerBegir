package com.amk.sakoo.ui.features.platformServicesScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.sakoo.model.data.ServiceItemsResponse
import com.amk.sakoo.model.repository.serviceItemsRepository.ServiceItemsRepository
import com.amk.sakoo.util.allPlatforms
import kotlinx.coroutines.launch

class PlatformServicesViewModel(
    private val servicesRepository: ServiceItemsRepository
) : ViewModel() {

    val servicesList = mutableStateOf<List<ServiceItemsResponse>>(emptyList())
    val isLoading = mutableStateOf(true)
    val isError = mutableStateOf(false)

    fun getPlatformServices(platformId: String) {
        viewModelScope.launch {
            servicesList.value = emptyList()
            isLoading.value = true
            isError.value = false

            val allowedServiceIds = allPlatforms.find { it.id == platformId }?.serviceIds ?: emptyList()

            if (allowedServiceIds.isEmpty()) {
                isLoading.value = false
                return@launch
            }

            try {
                val allApiServices = servicesRepository.getAllItemsList()
                val filteredServices = allApiServices.filter { it.service in allowedServiceIds }
                servicesList.value = filteredServices
            } catch (e: Exception) {
                isError.value = true
            } finally {
                isLoading.value = false
            }
        }
    }
}