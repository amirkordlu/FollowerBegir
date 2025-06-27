package com.amk.followerbegir.model.repository

import com.amk.followerbegir.model.data.AddOrderServiceResponse

interface AddOrderServiceRepository {
    suspend fun addOrderService(
        serviceId: Int,
        link: String,
        quantity: Int,
        isTest: Int
    ): AddOrderServiceResponse
}