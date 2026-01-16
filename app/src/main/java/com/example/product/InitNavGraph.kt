package com.example.product

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.product.component.AppScaffold
import com.example.product.core.ProductSharedViewModel
import com.example.product.navigation.NavDestination
import com.example.product.navigation.Screen
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

@Composable
fun InitNavGraph() {
    val backStack = remember { mutableStateListOf<Any>(Screen.Home) }
    NavDisplay(
        backStack = backStack, // Your custom-managed back stack
        modifier = Modifier,
        transitionSpec = { // Define custom transitions for screen changes
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
        ),
        entryProvider = entryProvider { // Define your screen entries here
            entry<Screen.Home> { // Entry for the Home screen
                AppScaffold { paddingValues ->
                    Home(padding = paddingValues, onCardClicked = { productResponse ->
                        backStack.add(Screen.ProductDetail(productResponse))
                    })

                }
            }
            entry<Screen.ProductDetail> {
                it // Entry for the Topics screen
                AppScaffold(
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    showBackArrow = true
                ) { paddingValues ->
                    it.product
                    ProductDetails(
                        padding = paddingValues,
                        product = it.product,
                        onCloseClick = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }

        }
    )
}