package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketCoinListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketDepthListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketTickerListener
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketTradeListener
import com.metoer.ceptedovizborsa.util.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val appApi: AppApi,
    private val providesOkhttpClient: OkHttpClient,
    private val providesBinanceWebSocketTickerListener: BinanceWebSocketTickerListener,
    private val providesBinanceWebSocketListener: BinanceWebSocketCoinListener,
    private val providesDepthListener: BinanceWebSocketDepthListener,
    private val providesTradeListener: BinanceWebSocketTradeListener
) {

    fun getChartFromBinanceApi(
        symbol: String,
        interval: String,
        limit: Int = Constants.BINANCE_CHART_LIMIT
    ) = appApi.getChartFromBinanceData(symbol, interval, limit)

    fun getRatesDataFromApi(apiKey: String) = appApi.getAllRatesData(apiKey)

    fun getTickerFromBinanceApi(
        symbol: String,
        windowSize: String,
        type: String? = null
    ) = appApi.getTickerFromBinanceData(symbol, windowSize, type)

    fun getPageTickerDataFromBinanceApi() = appApi.getPageTickerData()

    fun getCurrentAvaragePriceDataFromApi(symbol: String) = appApi.getCurrentAvaragePriceData(symbol)

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