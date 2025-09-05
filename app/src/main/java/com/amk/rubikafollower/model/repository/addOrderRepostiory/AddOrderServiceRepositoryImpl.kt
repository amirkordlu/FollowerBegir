package com.amk.rubikafollower.model.repository.addOrderRepostiory

import com.amk.rubikafollower.model.data.AddOrderServiceResponse
import com.amk.rubikafollower.model.net.ApiService

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