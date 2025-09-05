package com.amk.rubikafollower.model.repository.orderStatusRepository

import com.amk.rubikafollower.model.data.OrderStatusResponse

interface OrderStatusRepository {
    suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse>
}
