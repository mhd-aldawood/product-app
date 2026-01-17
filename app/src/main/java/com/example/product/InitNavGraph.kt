package com.example.product

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.product.component.AppScaffold
import com.example.product.navigation.Screen
import com.example.product.screen.deatils.ProductDetails
import com.example.product.screen.home.Home
import com.example.product.screen.home.HomeAction
import com.example.product.screen.home.HomeViewModel

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
                val viewModel: HomeViewModel = hiltViewModel()
                val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

                AppScaffold(
                    snackbarMessage = uiState.netWorkMsg,
                ) { paddingValues ->
                    Home(
                        padding = paddingValues, onCardClicked = { productResponse ->
                            backStack.add(Screen.ProductDetail(productResponse))
                            viewModel.trySendAction(HomeAction.ResetNetwork)
                        },
                        uiState = uiState,
                        onRefresh = { viewModel.trySendAction(HomeAction.OnRefreshProduct) },
                        onCategoriesClicked = {
                            viewModel.trySendAction(
                                HomeAction.OnCategoriesClicked(
                                    it
                                )
                            )
                        }
                    )

                }
            }
            entry<Screen.ProductDetail> {
                it
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