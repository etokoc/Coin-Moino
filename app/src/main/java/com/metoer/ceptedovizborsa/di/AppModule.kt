package com.metoer.ceptedovizborsa.di

import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesCurrencyRepository(appApi: AppApi): CurrencyRepository {
        return CurrencyRepository(appApi)
    }

    @Provides
    @Singleton
    fun providesAppApi(): AppApi {
        return ApiNetworkAdapter.appApi()
    }
}