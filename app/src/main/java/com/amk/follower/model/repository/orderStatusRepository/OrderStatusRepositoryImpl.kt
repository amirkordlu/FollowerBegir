package com.amk.follower.model.repository.orderStatusRepository

import com.amk.follower.model.data.OrderStatusResponse
import com.amk.follower.model.net.ApiService

class OrderStatusRepositoryImpl(private val apiService: ApiService) : OrderStatusRepository {

    override suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse> {
        return apiService.getOrderStatus(orders = orders)
    }
}
