package com.example.product.repo

import com.example.product.repo.model.ProductResponse
import com.example.product.screen.home.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getProducts(): Flow<Result<List<ProductResponse>>> = flow {
        try {
            val products = api.getProducts()
            emit(Result.Success(data = products))
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }
}