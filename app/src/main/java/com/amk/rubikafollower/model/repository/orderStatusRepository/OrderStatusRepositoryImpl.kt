package com.amk.rubikafollower.model.repository.orderStatusRepository

import com.amk.rubikafollower.model.data.OrderStatusResponse
import com.amk.rubikafollower.model.net.ApiService

class OrderStatusRepositoryImpl(private val apiService: ApiService) : OrderStatusRepository {

    override suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse> {
        return apiService.getOrderStatus(orders = orders)
    }
}
