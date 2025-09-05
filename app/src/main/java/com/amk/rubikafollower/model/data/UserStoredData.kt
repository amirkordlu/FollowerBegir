package com.amk.rubikafollower.model.data

import kotlinx.serialization.Serializable


@Serializable
data class UserStoredData(
    val wallet: Int,
    val orderNumbers: List<Int>
)
