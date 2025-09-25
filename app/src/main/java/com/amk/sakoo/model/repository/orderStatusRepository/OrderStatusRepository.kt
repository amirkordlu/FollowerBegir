package com.amk.sakoo.model.repository.orderStatusRepository

import com.amk.sakoo.model.data.OrderStatusResponse

interface OrderStatusRepository {
    suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse>
}
