package com.amk.follower.model.data

import kotlinx.serialization.Serializable


@Serializable
data class UserStoredData(
    val wallet: Int,
    val orderNumbers: List<Int>
)
