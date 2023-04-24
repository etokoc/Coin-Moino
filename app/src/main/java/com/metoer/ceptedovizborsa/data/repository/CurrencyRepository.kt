package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.webscoket.*
import com.metoer.ceptedovizborsa.util.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    val appApi: AppApi,
    private val providesOkhttpClient: OkHttpClient,
    val providesBinanceWebSocketTickerListener: BinanceWebSocketTickerListener,
    val providesBinanceWebSocketListener: BinanceWebSocketCoinListener,
    val providesDepthListener: BinanceWebSocketDepthListener,
    val providesTradeListener: BinanceWebSocketTradeListener
) {
    fun getCurrencyDataFromApi(timeUnix: String) = appApi.getCurrencyData(timeUnix)

    fun getChartFromBinanceApi(
        symbol: String,
        interval: String,
        limit: Int = Constants.BINANCE_CHART_LIMIT
    ) = appApi.getChartFromBinanceData(symbol, interval, limit)

    fun getRatesDataFromApi(apiKey: String) = appApi.getAllRatesData(apiKey)

    fun getTickerFromBinanceApi(
        symbol: String,
        windowSize: String
    ) = appApi.getTickerFromBinanceData(symbol, windowSize)

    fun getPageTickerDataFromBinanceApi() = appApi.getPageTickerData()

    fun getBinanceTickerSocket(
        symbol: String,
        webSocketType: String,
        param: String = ""
    ): WebSocket {
        val request: Request =
            Request.Builder()
                .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${symbol.lowercase()}$webSocketType$param")
                .build()
        return providesOkhttpClient.newWebSocket(
            request,
            providesBinanceWebSocketTickerListener
        )
    }

    fun getBinanceSocketTickerListener() = providesBinanceWebSocketTickerListener


    fun getBinanceDepthSocket(symbol: String): WebSocket {
        val request: Request = Request.Builder()
            .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${symbol.lowercase()}@depth20")
            .build()
        return providesOkhttpClient.newWebSocket(
            request,
            providesDepthListener
        )
    }

    fun getBinanceSocketDepthListener() = providesDepthListener

    fun getBinanceTradeSocket(symbol: String): WebSocket {
        val request: Request = Request.Builder()
            .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${symbol.lowercase()}@trade")
            .build()
        return providesOkhttpClient.newWebSocket(
            request,
            providesTradeListener
        )
    }

    fun getBinanceSocketTradeListener() = providesTradeListener

    fun getBinanceCoinSocket(): WebSocket {
        val request: Request =
            Request.Builder()
                .url(Constants.BINANCE_WEB_SOCKET_COIN_BASE_URL)
                .build()
        return providesOkhttpClient.newWebSocket(request, providesBinanceWebSocketListener)
    }

    fun getBinanceSocketListener() = providesBinanceWebSocketListener

}