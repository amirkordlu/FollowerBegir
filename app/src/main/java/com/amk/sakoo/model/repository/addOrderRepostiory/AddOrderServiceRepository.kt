package com.amk.sakoo.model.repository.addOrderRepostiory

import com.amk.sakoo.model.data.AddOrderServiceResponse

interface AddOrderServiceRepository {
    suspend fun addOrderService(
        serviceId: Int,
        link: String,
        quantity: Int,
        isTest: Int
    ): AddOrderServiceResponse
}