package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.response.coin.Ticker.CoinTickerResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.webscoket.*
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
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


    fun getBinanceDepthSocket(baseSymbol: String, quoteSymbol: String): WebSocket {
        val request: Request = Request.Builder()
            .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${baseSymbol.lowercase() + quoteSymbol.lowercase()}@depth20")
            .build()
        return providesOkhttpClient.newWebSocket(
            request,
            providesDepthListener
        )
    }

    fun getBinanceSocketDepthListener() = providesDepthListener

    fun getBinanceTradeSocket(baseSymbol: String, quoteSymbol: String): WebSocket {
        val request: Request = Request.Builder()
            .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${baseSymbol.lowercase() + quoteSymbol.lowercase()}@trade")
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

    fun getPageTickerDataFromBinanceApi ()  = appApi.getPageTickerData()
}