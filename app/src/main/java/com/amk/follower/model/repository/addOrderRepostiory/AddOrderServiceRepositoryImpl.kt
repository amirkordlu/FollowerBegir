package com.amk.follower.model.repository.addOrderRepostiory

import com.amk.follower.model.data.AddOrderServiceResponse
import com.amk.follower.model.net.ApiService

class AddOrderServiceRepositoryImpl(private val apiService: ApiService) :
    AddOrderServiceRepository {

    override suspend fun addOrderService(
        serviceId: Int,
        link: String,
        quantity: Int,
        isTest: Int
    ): AddOrderServiceResponse {
        return apiService.addOrderService(
            service = serviceId,
            link = link,
            quantity = quantity,
            isTest = isTest
        )
    }

}