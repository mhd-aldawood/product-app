package com.example.product.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.product.core.BaseViewModel
import com.example.product.repo.ProductRepository
import com.example.product.repo.model.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val productResponse: List<ProductResponse> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCategory: String? = null,
    val filteredList: List<ProductResponse> = emptyList(),
    val categories: List<String> = emptyList(),
)

sealed class HomeAction {
    data object OnRefreshProduct : HomeAction()
    data class OnCategoriesClicked(val value: String?) : HomeAction()
}

data object HomeEvents

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ProductRepository,
) :
    BaseViewModel<HomeState, HomeEvents, HomeAction>(
        initialState = HomeState()
    ) {
    init {
        viewModelScope.launch {
            fetchProductList()
        }

    }

    private val TAG = "HomeViewModel"
    private fun fetchProductList() {
        viewModelScope.launch {
            repo.getProducts().collect() {
                when (it) {
                    is Result.Success -> {
                        Log.i(TAG, "fetchProductList:${it.data} ")
                        val categories = getDistinctCategories(it.data)
                        mutableState.update { it1 ->
                            it1.copy(
                                filteredList = it.data,
                                productResponse = it.data,
                                isLoading = false,
                                categories = categories,
                            )
                        }
                    }

                    is Result.Error -> {
                    }

                    Result.Loading -> {}
                }

            }
        }
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnCategoriesClicked -> {
                handleOnCategorySelected(action.value)
            }

            is HomeAction.OnRefreshProduct -> handleRefreshProduct()
        }
    }

    //handle the selected category then change the the displayed list
    fun handleOnCategorySelected(category: String?) {
        mutableState.update { current ->
            val filtered = if (category == null) {
                current.productResponse
            } else {
                current.productResponse.filter {
                    it.category == category
                }
            }

            current.copy(
                selectedCategory = category,
                filteredList = filtered
            )
        }
    }

    fun handleRefreshProduct() {
        mutableState.update {
            it.copy(
                isLoading = true
            )
        }
        fetchProductList()
    }

    fun getDistinctCategories(products: List<ProductResponse>): List<String> {
        return products
            .mapNotNull { it.category }
            .distinct()
    }

}

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}