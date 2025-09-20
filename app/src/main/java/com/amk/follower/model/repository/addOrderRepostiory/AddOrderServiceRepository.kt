package com.amk.follower.model.repository.addOrderRepostiory

import com.amk.follower.model.data.AddOrderServiceResponse

interface AddOrderServiceRepository {
    suspend fun addOrderService(
        serviceId: Int,
        link: String,
        quantity: Int,
        isTest: Int
    ): AddOrderServiceResponse
}