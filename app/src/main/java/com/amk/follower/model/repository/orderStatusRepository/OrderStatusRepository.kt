package com.amk.follower.model.repository.orderStatusRepository

import com.amk.follower.model.data.OrderStatusResponse

interface OrderStatusRepository {
    suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse>
}
