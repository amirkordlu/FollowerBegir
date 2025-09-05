package com.amk.rubikafollower.model.repository.serviceItemsRepository

import com.amk.rubikafollower.model.data.ServiceItemsResponse
import com.amk.rubikafollower.model.net.ApiService

class ServiceItemsRepositoryImpl(private val apiService: ApiService) : ServiceItemsRepository {

    override suspend fun getAllItemsList(): List<ServiceItemsResponse> {
        return apiService.getAllItems()
    }

}