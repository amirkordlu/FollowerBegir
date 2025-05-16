package com.amk.followerbegir.model.repository

import com.amk.followerbegir.model.data.ServiceItemsResponse
import com.amk.followerbegir.model.net.ApiService

class ServiceItemsRepositoryImpl(private val apiService: ApiService) : ServiceItemsRepository {

    override suspend fun getAllItemsList(): List<ServiceItemsResponse> {
        return apiService.getAllItems()
    }

}