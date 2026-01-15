package com.example.product.navigation

import androidx.navigation.NavController

fun NavController.safeNavigate(
    destination: String
) {

    fun extractBeforeQuestionMark(input: String?): String {
        return input?.substringBefore("?") ?: ""
    }

    // Check if the current route is not the same as the destination
    if (extractBeforeQuestionMark(currentBackStackEntry?.destination?.route) != extractBeforeQuestionMark(
            destination
        )
    ) {
        navigate(destination)
    }
}

fun NavController.navigateToProductDetails() {
    safeNavigate(NavDestination.PRODUCT_DETAILS)
}