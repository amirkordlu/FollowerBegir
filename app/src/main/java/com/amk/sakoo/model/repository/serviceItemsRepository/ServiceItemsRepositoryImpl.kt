package com.amk.sakoo.model.repository.serviceItemsRepository

import com.amk.sakoo.model.data.ServiceItemsResponse
import com.amk.sakoo.model.net.ApiService

class ServiceItemsRepositoryImpl(private val apiService: ApiService) : ServiceItemsRepository {

    override suspend fun getAllItemsList(): List<ServiceItemsResponse> {
        return apiService.getAllItems()
    }

}