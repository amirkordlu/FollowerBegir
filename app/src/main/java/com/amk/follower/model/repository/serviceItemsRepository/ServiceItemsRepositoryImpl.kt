package com.amk.follower.model.repository.serviceItemsRepository

import com.amk.follower.model.data.ServiceItemsResponse
import com.amk.follower.model.net.ApiService

class ServiceItemsRepositoryImpl(private val apiService: ApiService) : ServiceItemsRepository {

    override suspend fun getAllItemsList(): List<ServiceItemsResponse> {
        return apiService.getAllItems()
    }

}