package com.metoer.ceptedovizborsa.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.QualifiedTypeConverterFactory
import com.metoer.ceptedovizborsa.data.db.CoinBuyDatabase
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinWebSocketResponse
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketChartListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketCoinListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketTickerListener
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        return CurrencyRepository(
            appApi, providesOkhttpClient(), providesBinanceTickerSocket(),
            providesBinanceChartSocket(),
            providesBinanceCoinSocket())
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
    fun providesRetrofit(providesOkHttpClient: OkHttpClient.Builder): Retrofit {
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
    fun providesAppApi(retrofit: Retrofit) = retrofit.create(AppApi::class.java)

    @Provides
    @Singleton
    fun providesCoinDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CoinBuyDatabase::class.java, "coin_buy_db").build()

    @Provides
    @Singleton
    fun provideYourDao(db: CoinBuyDatabase) = db.getCoinBuyDao()


    @Provides
    @Singleton
    fun providesBinanceTickerSocket() = BinanceWebSocketTickerListener()

    @Provides
    @Singleton
    fun providesBinanceCoinSocket() = BinanceWebSocketCoinListener()

    @Provides
    @Singleton
    fun providesBinanceChartSocket() = BinanceWebSocketChartListener()

    @Provides
    @Singleton
    fun providesOkhttpClient() = OkHttpClient()
}