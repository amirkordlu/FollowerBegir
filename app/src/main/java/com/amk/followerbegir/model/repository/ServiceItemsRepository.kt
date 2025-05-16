package com.amk.followerbegir.model.repository

import com.amk.followerbegir.model.data.ServiceItemsResponse

interface ServiceItemsRepository {
    suspend fun getAllItemsList(): List<ServiceItemsResponse>
}