package com.amk.follower.model.repository.serviceItemsRepository

import com.amk.follower.model.data.ServiceItemsResponse

interface ServiceItemsRepository {
    suspend fun getAllItemsList(): List<ServiceItemsResponse>
}