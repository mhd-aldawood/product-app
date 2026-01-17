package com.example.product.di

import android.content.Context
import com.example.product.core.NetworkStateListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkStateModule {
    @Provides
    @Singleton
    fun provideNetworkState(@ApplicationContext context: Context): NetworkStateListener =
        NetworkStateListener(context)
}