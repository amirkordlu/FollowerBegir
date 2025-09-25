package com.amk.sakoo.ui.features.homeScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.sakoo.model.data.ServiceItemsResponse
import com.amk.sakoo.model.repository.serviceItemsRepository.ServiceItemsRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val allServiceItems: ServiceItemsRepository
) : ViewModel() {

    val servicesList = mutableStateOf<List<ServiceItemsResponse>>(emptyList())
    val isLoading = mutableStateOf(true)
    val isError = mutableStateOf(false)

    fun getAllItemsService() {
        viewModelScope.launch {
            isLoading.value = true
            isError.value = false
            try {
                val data = allServiceItems.getAllItemsList()
                servicesList.value = data
            } catch (_: Exception) {
                isError.value = true
            } finally {
                isLoading.value = false
            }
        }
    }
}

