package com.metoer.ceptedovizborsa.di

import android.util.Log
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.QualifiedTypeConverterFactory
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
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
    fun providesSharedViewModel(): SharedViewModel {
        return SharedViewModel()
    }

    @Provides
    @Singleton
    fun providesOkhttp(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor { message ->
                Log.d(
                    "tag_retrofit_intercept",
                    "log: http log: $message"
                )
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
        return builder
    }

    @Provides
    @Singleton
    fun providesRetrofit (providesOkHttpClient: OkHttpClient.Builder) : Retrofit{
        val retrofit = Retrofit
            .Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                QualifiedTypeConverterFactory(
                    GsonConverterFactory.create(),
                    SimpleXmlConverterFactory.create()
                )
            )
            .baseUrl(Constants.CURRENCY_BASE_URL)
            .client(providesOkHttpClient.build())
            .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun providesAppApi(retrofit: Retrofit) =retrofit.create(AppApi::class.java)


}