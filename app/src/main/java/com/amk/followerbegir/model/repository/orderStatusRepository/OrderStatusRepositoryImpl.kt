package com.amk.followerbegir.model.repository.orderStatusRepository

import com.amk.followerbegir.model.data.OrderStatusResponse
import com.amk.followerbegir.model.net.ApiService

class OrderStatusRepositoryImpl(private val apiService: ApiService) : OrderStatusRepository {

    override suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse> {
        return apiService.getOrderStatus(orders = orders)
    }
}
