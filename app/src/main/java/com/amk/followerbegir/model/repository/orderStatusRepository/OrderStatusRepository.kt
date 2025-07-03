package com.amk.followerbegir.model.repository.orderStatusRepository

import com.amk.followerbegir.model.data.OrderStatusResponse

interface OrderStatusRepository {
    suspend fun getOrdersStatus(orders: String): Map<String, OrderStatusResponse>
}
