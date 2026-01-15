package com.example.product.repo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    @SerialName("rate") var rate: Double? = null,
    @SerialName("count") var count: Int? = null
)