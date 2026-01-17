package com.example.product.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.product.core.BaseViewModel
import com.example.product.core.NetWorkStatus
import com.example.product.core.NetworkStateListener
import com.example.product.repo.ProductRepository
import com.example.product.repo.model.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val productResponse: List<ProductResponse> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val selectedCategory: String? = null,
    val filteredList: List<ProductResponse> = emptyList(),
    val categories: List<String> = emptyList(),
    val netWorkMsg: String? = null
)

sealed class HomeAction {
    data object OnRefreshProduct : HomeAction()
    data object ResetNetwork : HomeAction()

    data class OnCategoriesClicked(val value: String?) : HomeAction()
}

data object HomeEvents

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val netWorkStatus: NetworkStateListener
) :
    BaseViewModel<HomeState, HomeEvents, HomeAction>(
        initialState = HomeState()
    ) {
    var currentNetwork: NetWorkStatus? = null

    init {
        viewModelScope.launch {
            observeNetworkStatus()
        }
    }

    // ------------------------------
    // Network Status Handling
    // ------------------------------
    suspend fun observeNetworkStatus() {
        netWorkStatus.getNetworkStatusFlow().collect {
            currentNetwork = it
            when (it) {
                NetWorkStatus.NetworkHasNoInternet -> {
                    handleNetworkLostOrUnavailable(it)
                }

                NetWorkStatus.NetworkHasInternet -> {
                    fetchProductList()
                }

                NetWorkStatus.NetworkLost -> {
                    handleNetworkLostOrUnavailable(it)
                }

                NetWorkStatus.NetworkAvailable -> {
                }
            }
        }
    }

    fun updateNetworkStatus(msg: String?) {
        mutableState.update { it1 ->
            it1.copy(netWorkMsg = msg)
        }
    }

    private val TAG = "HomeViewModel"
    private fun fetchProductList() {
        viewModelScope.launch {
            repo.getProducts().collect() {
                when (it) {
                    is Result.Success -> {
                        handleProductSuccess(it.data)
                    }

                    is Result.Error -> {
                        Log.e(TAG, it.message)
                    }

                    Result.Loading -> {}
                }

            }
        }
    }

    private fun handleProductSuccess(products: List<ProductResponse>) {
        val categories = getDistinctCategories(products)
        mutableState.update {
            it.copy(
                filteredList = products,
                productResponse = products,
                isLoading = false,
                isRefreshing = false,
                categories = categories
            )
        }
    }

    private fun handleNetworkLostOrUnavailable(status: NetWorkStatus) {
        mutableState.update { it.copy(isLoading = false) }
        updateNetworkStatus(status.msg)
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnCategoriesClicked -> {
                handleOnCategorySelected(action.value)
            }

            is HomeAction.OnRefreshProduct -> handleRefreshProduct()
            is HomeAction.ResetNetwork -> {
                updateNetworkStatus(null)
            }
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
        if (currentNetwork == NetWorkStatus.NetworkHasInternet) {
            mutableState.update {
                it.copy(
                    isRefreshing = true
                )
            }
            fetchProductList()
        } else {
            currentNetwork?.msg?.let {
                updateNetworkStatus(it)
            }
        }
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