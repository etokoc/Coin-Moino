package com.metoer.ceptedovizborsa.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.paging.AllCoinDataSoruce
import com.metoer.ceptedovizborsa.data.response.coin.Ticker.CoinTickerResponse
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketChartListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketCoinListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketTickerListener
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val appApi: AppApi,
    private val providesOkhttpClient: OkHttpClient,
    val providesBinanceWebSocketTickerListener: BinanceWebSocketTickerListener,
    val providesBinanceWebSocketChartListener: BinanceWebSocketChartListener,
    val providesBinanceWebSocketListener: BinanceWebSocketCoinListener
) {
    fun getCurrencyDataFromApi(timeUnix: String) = appApi.getCurrencyData(timeUnix)
    fun getAllCoinDataFromApi(): LiveData<PagingData<CoinData>> {
        val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)
        val myData: LiveData<PagingData<CoinData>> = Pager(pagingConfig) {
            AllCoinDataSoruce(appApi)
        }.liveData
        return myData
    }

    fun getAllMarketsCoinDataFromApi(apiKey: String, quoteSymbol: String) =
        appApi.getAllMarketsCoinData(apiKey, quoteSymbol)

    fun getChartFromBinanceApi(
        symbol: String,
        interval: String,
        limit: Int = Constants.BINANCE_CHART_LIMIT
    ): Observable<BinanceRoot> {
        return appApi.getChartFromBinanceData(symbol, interval, limit)
    }

    fun getRatesDataFromApi(apiKey: String) = appApi.getAllRatesData(apiKey)

    fun getTickerFromBinanceApi(
        symbol: String,
        windowSize: String
    ): Observable<CoinTickerResponse> {
        return appApi.getTickerFromBinanceData(symbol, windowSize)
    }

    fun getAllCoinCandlesDataFromApi(
        apiKey: String,
        interval: String,
        baseId: String,
        quetoId: String
    ) = appApi.getAllCandlesCoinData(apiKey, interval, baseId, quetoId)


    fun getBinanceTickerSocket(
        baseSymbol: String,
        quoteSymbol: String,
        webSocketType: String,
        param: String = ""
    ): WebSocket {
        val request: Request =
            Request.Builder()
                .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${baseSymbol.lowercase() + quoteSymbol.lowercase()}$webSocketType$param")
                .build()
        return providesOkhttpClient.newWebSocket(
            request,
            providesBinanceWebSocketTickerListener
        )
    }

    fun getBinanceSocketTickerListener() = providesBinanceWebSocketTickerListener

    fun getBinanceChartSocket(
        baseSymbol: String,
        quoteSymbol: String,
        webSocketType: String,
        param: String = ""
    ): WebSocket {
        val request: Request =
            Request.Builder()
                .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${baseSymbol.lowercase() + quoteSymbol.lowercase()}$webSocketType$param")
                .build()
        return providesOkhttpClient.newWebSocket(request, providesBinanceWebSocketChartListener)
    }

    fun getBinanceSocketChartListener() = providesBinanceWebSocketChartListener

    fun getBinanceCoinSocket(): WebSocket {
        val request: Request =
            Request.Builder()
                .url(Constants.BINANCE_WEB_SOCKET_COIN_BASE_URL)
                .build()
        return providesOkhttpClient.newWebSocket(request, providesBinanceWebSocketListener)
    }

    fun getBinanceSocketListener() = providesBinanceWebSocketListener
}