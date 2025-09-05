package com.amk.rubikafollower.model.repository.addOrderRepostiory

import com.amk.rubikafollower.model.data.AddOrderServiceResponse

interface AddOrderServiceRepository {
    suspend fun addOrderService(
        serviceId: Int,
        link: String,
        quantity: Int,
        isTest: Int
    ): AddOrderServiceResponse
}