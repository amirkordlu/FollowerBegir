package com.amk.followerbegir.model.data

data class OrderStatusResponse(
    val order: Int,
    val status: String,
    val charge: String,
    val start_count: Any?,
    val remains: Any?
)
