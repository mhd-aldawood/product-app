package com.example.product.repo

import com.example.product.repo.model.ProductResponse
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductResponse>
}