package com.metoer.ceptedovizborsa.di

import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.view.fragment.CoinFragment
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
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
    @Provides
    @Singleton
    fun providesSharedViewModel(): SharedViewModel {
        return SharedViewModel()
    }


}