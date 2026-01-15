package com.example.product.core

import androidx.lifecycle.ViewModel
import com.example.product.repo.model.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductSharedViewModel : ViewModel() {
    private val _data = MutableStateFlow<ProductResponse?>(null)
    val data: StateFlow<ProductResponse?> get() = _data

    fun set(value: ProductResponse) {
        _data.value = value
    }

    fun get(): ProductResponse? = _data.value

    fun clear() {
        _data.value = null
    }
}
