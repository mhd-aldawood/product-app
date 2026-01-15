package com.example.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.product.component.AppScaffold
import com.example.product.core.ProductSharedViewModel
import com.example.product.navigation.NavDestination
import com.example.product.navigation.navigateToProductDetails
import com.example.product.screen.deatils.ProductDetails
import com.example.product.screen.home.Home
import kotlinx.coroutines.CoroutineScope

@Composable
fun InitNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = NavDestination.HOME_SCREEN
) {
    val sharedViewModel: ProductSharedViewModel = hiltViewModel() // activity-scoped

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(NavDestination.HOME_SCREEN) {
            AppScaffold { paddingValues ->
                Home(padding = paddingValues, onCardClicked = { productResponse ->

                    sharedViewModel.set(productResponse) // set selected product

                    navController.navigateToProductDetails()
                })

            }
        }
        composable(NavDestination.PRODUCT_DETAILS) {
            AppScaffold(
                onBackClick = { navController.popBackStack() },
                showBackArrow = true
            ) { paddingValues ->
                val product = sharedViewModel.get()
                product?.let {
                    ProductDetails(
                        padding = paddingValues,
                        product = it,
                        onCloseClick = {
                            sharedViewModel.clear()
                            navController.popBackStack()
                        }
                    )
                }
            }

        }

    }
}

