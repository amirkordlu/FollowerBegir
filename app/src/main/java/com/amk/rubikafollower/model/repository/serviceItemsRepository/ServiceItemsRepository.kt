package com.amk.rubikafollower.model.repository.serviceItemsRepository

import com.amk.rubikafollower.model.data.ServiceItemsResponse

interface ServiceItemsRepository {
    suspend fun getAllItemsList(): List<ServiceItemsResponse>
}