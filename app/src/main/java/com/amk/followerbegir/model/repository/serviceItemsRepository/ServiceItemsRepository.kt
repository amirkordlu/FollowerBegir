package com.amk.followerbegir.model.repository.serviceItemsRepository

import com.amk.followerbegir.model.data.ServiceItemsResponse

interface ServiceItemsRepository {
    suspend fun getAllItemsList(): List<ServiceItemsResponse>
}