package com.amk.follower.model.data

data class ServiceItemsResponse(
    val cancel: Boolean,
    val category: String,
    val desc: String,
    val dripfeed: Boolean,
    val max: Int,
    val min: Int,
    val name: String,
    val rate: Int,
    val refill: Boolean,
    val service: String,
    val service_rate: String,
    val template_link: String,
    val type: String
)