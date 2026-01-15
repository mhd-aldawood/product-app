package com.example.product.screen.home

import androidx.lifecycle.viewModelScope
import com.example.product.core.BaseViewModel
import com.example.product.repo.ProductRepository
import com.example.product.repo.model.ProductResponse
import com.example.product.repo.model.Rating
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
    val fakeProducts: List<ProductResponse> = listOf(
        ProductResponse(
            id = 1,
            title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
            price = 109.95,
            description = "Your perfect pack for everyday use and walks in the forest.",
            category = "men's clothing",
            image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
            rating = Rating(3.9, 120)
        ),
        ProductResponse(
            id = 2,
            title = "Mens Casual Premium Slim Fit T-Shirts",
            price = 22.3,
            description = "Slim-fitting style, contrast raglan long sleeve.",
            category = "men's clothing",
            image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
            rating = Rating(4.1, 259)
        ),
        ProductResponse(
            id = 3,
            title = "Mens Cotton Jacket",
            price = 55.99,
            description = "Great outerwear jackets for Spring/Autumn/Winter.",
            category = "men's clothing",
            image = "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_t.png",
            rating = Rating(4.7, 500)
        ),
        ProductResponse(
            id = 4,
            title = "Mens Casual Slim Fit",
            price = 15.99,
            description = "The color could be slightly different between screen and practice.",
            category = "men's clothing",
            image = "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_t.png",
            rating = Rating(2.1, 430)
        ),
        ProductResponse(
            id = 5,
            title = "John Hardy Women's Legends Naga Bracelet",
            price = 695.0,
            description = "Inspired by the mythical water dragon.",
            category = "jewelery",
            image = "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_t.png",
            rating = Rating(4.6, 400)
        ),
        ProductResponse(
            id = 6,
            title = "Solid Gold Petite Micropave",
            price = 168.0,
            description = "Satisfaction guaranteed. Return or exchange within 30 days.",
            category = "jewelery",
            image = "https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_t.png",
            rating = Rating(3.9, 70)
        ),
        ProductResponse(
            id = 7,
            title = "White Gold Plated Princess Ring",
            price = 9.99,
            description = "Classic created wedding engagement ring.",
            category = "jewelery",
            image = "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_t.png",
            rating = Rating(3.0, 400)
        ),
        ProductResponse(
            id = 8,
            title = "Pierced Owl Rose Gold Plated Earrings",
            price = 10.99,
            description = "Rose gold plated stainless steel tunnel plug earrings.",
            category = "jewelery",
            image = "https://fakestoreapi.com/img/51UDEzMJVpL._AC_UL640_QL65_ML3_t.png",
            rating = Rating(1.9, 100)
        ),
        ProductResponse(
            id = 9,
            title = "WD 2TB Elements Portable External Hard Drive",
            price = 64.0,
            description = "USB 3.0 and USB 2.0 compatibility.",
            category = "electronics",
            image = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png",
            rating = Rating(3.3, 203)
        ),
        ProductResponse(
            id = 10,
            title = "SanDisk SSD PLUS 1TB Internal SSD",
            price = 109.0,
            description = "Easy upgrade for faster boot up and performance.",
            category = "electronics",
            image = "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_t.png",
            rating = Rating(2.9, 470)
        ),
        ProductResponse(
            id = 11,
            title = "Silicon Power 256GB SSD",
            price = 109.0,
            description = "High transfer speeds and improved system performance.",
            category = "electronics",
            image = "https://fakestoreapi.com/img/71kWymZ+c+L._AC_SX679_t.png",
            rating = Rating(4.8, 319)
        ),
        ProductResponse(
            id = 12,
            title = "WD 4TB Gaming Drive for PS4",
            price = 114.0,
            description = "Expand your PS4 gaming experience.",
            category = "electronics",
            image = "https://fakestoreapi.com/img/61mtL65D4cL._AC_SX679_t.png",
            rating = Rating(4.8, 400)
        ),
        ProductResponse(
            id = 13,
            title = "Acer SB220Q 21.5\" Full HD Monitor",
            price = 599.0,
            description = "Ultra-thin IPS display with 75Hz refresh rate.",
            category = "electronics",
            image = "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_t.png",
            rating = Rating(2.9, 250)
        ),
        ProductResponse(
            id = 14,
            title = "Samsung 49-Inch Curved Gaming Monitor",
            price = 999.99,
            description = "Super ultrawide curved QLED gaming monitor.",
            category = "electronics",
            image = "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_t.png",
            rating = Rating(2.2, 140)
        ),
        ProductResponse(
            id = 15,
            title = "BIYLACLESEN Women's 3-in-1 Snowboard Jacket",
            price = 56.99,
            description = "Detachable liner jacket suitable for different seasons.",
            category = "women's clothing",
            image = "https://fakestoreapi.com/img/51Y5NI-I5jL._AC_UX679_t.png",
            rating = Rating(2.6, 235)
        ),
        ProductResponse(
            id = 16,
            title = "Lock and Love Women's Faux Leather Jacket",
            price = 29.95,
            description = "Removable hooded faux leather moto biker jacket.",
            category = "women's clothing",
            image = "https://fakestoreapi.com/img/81XH0e8fefL._AC_UY879_t.png",
            rating = Rating(2.9, 340)
        ),
        ProductResponse(
            id = 17,
            title = "Rain Jacket Women Windbreaker",
            price = 39.99,
            description = "Lightweight hooded raincoat with adjustable waist.",
            category = "women's clothing",
            image = "https://fakestoreapi.com/img/71HblAHs5xL._AC_UY879_-2t.png",
            rating = Rating(3.8, 679)
        ),
        ProductResponse(
            id = 18,
            title = "MBJ Women's Solid Short Sleeve Boat Neck",
            price = 9.85,
            description = "Lightweight fabric with great stretch for comfort.",
            category = "women's clothing",
            image = "https://fakestoreapi.com/img/71z3kpMAYsL._AC_UY879_t.png",
            rating = Rating(4.7, 130)
        ),
        ProductResponse(
            id = 19,
            title = "Opna Women's Short Sleeve Moisture Shirt",
            price = 7.95,
            description = "Lightweight, breathable, moisture-wicking fabric.",
            category = "women's clothing",
            image = "https://fakestoreapi.com/img/51eg55uWmdL._AC_UX679_t.png",
            rating = Rating(4.5, 146)
        ),
        ProductResponse(
            id = 20,
            title = "DANVOUY Womens T Shirt Casual Cotton",
            price = 12.99,
            description = "Soft cotton casual t-shirt for all seasons.",
            category = "women's clothing",
            image = "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_t.png",
            rating = Rating(3.6, 145)
        )
    ),
)

sealed class HomeAction {
    data object OnRefreshProduct : HomeAction()
    data class OnCategoriesClicked(val value: String?) : HomeAction()
}

sealed class HomeEvents {
}

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
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}