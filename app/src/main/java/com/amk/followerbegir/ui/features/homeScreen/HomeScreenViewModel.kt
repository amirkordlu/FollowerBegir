package com.amk.followerbegir.ui.features.homeScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.model.repository.ServiceItemsRepository
import com.amk.followerbegir.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val allServiceItems: ServiceItemsRepository
) : ViewModel() {

    val servicesList = mutableStateOf<List<ServiceItemsResponse>>(listOf())

    fun getAllItemsService() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val data = allServiceItems.getAllItemsList()
            servicesList.value = data
        }
    }

}