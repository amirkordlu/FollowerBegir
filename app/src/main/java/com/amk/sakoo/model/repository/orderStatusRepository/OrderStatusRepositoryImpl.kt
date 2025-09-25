package com.amk.sakoo.model.repository.orderStatusRepository

import com.amk.sakoo.model.data.OrderStatusResponse
import com.amk.sakoo.model.net.ApiService

class OrderStatusRepositoryImpl(private val apiService: ApiService) : OrderStatusRepository {

    override suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse> {
        return apiService.getOrderStatus(orders = orders)
    }
}
