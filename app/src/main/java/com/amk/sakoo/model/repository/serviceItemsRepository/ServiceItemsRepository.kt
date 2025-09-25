package com.amk.sakoo.model.repository.serviceItemsRepository

import com.amk.sakoo.model.data.ServiceItemsResponse

interface ServiceItemsRepository {
    suspend fun getAllItemsList(): List<ServiceItemsResponse>
}