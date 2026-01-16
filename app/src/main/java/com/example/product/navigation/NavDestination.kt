package com.example.product.navigation

import androidx.navigation3.runtime.NavKey
import com.example.product.repo.model.ProductResponse
import kotlinx.serialization.Serializable

object NavDestination {
    const val HOME_SCREEN = "HOME_SCREEN"
    const val PRODUCT_DETAILS = "PRODUCT_DETAILS"
}

sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data class ProductDetail(val product: ProductResponse) : Screen
}