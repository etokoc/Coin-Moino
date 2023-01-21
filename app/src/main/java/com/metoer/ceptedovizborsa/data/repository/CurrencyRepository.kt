package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.webscoket.BinanceWebSocketListener
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val appApi: AppApi,
    private val providesOkhttpClient: OkHttpClient,
    val providesBinanceWebSocketListener: BinanceWebSocketListener
) {
    fun getCurrencyDataFromApi(timeUnix: String) = appApi.getCurrencyData(timeUnix)
    fun getAllCoinDataFromApi() = appApi.getAllCoinData()
    fun getAllMarketsCoinDataFromApi(apiKey: String, quoteSymbol: String) =
        appApi.getAllMarketsCoinData(apiKey, quoteSymbol)

    fun getChartFromBinanceApi(
        symbol: String,
        interval: String,
        limit: Int = Constants.BINANCE_CHART_LIMIT
    ): Observable<BinanceRoot> {
        return appApi.getChartFromBinanceData(symbol, interval, limit)
    }

    fun getAllCoinCandlesDataFromApi(
        apiKey: String,
        interval: String,
        baseId: String,
        quetoId: String
    ) = appApi.getAllCandlesCoinData(apiKey, interval, baseId, quetoId)


    fun getBinanceSocket(
        baseSymbol: String,
        quoteSymbol: String,
        webSocketType: String,
        param: String = ""
    ): WebSocket {
        val request: Request =
            Request.Builder()
                .url("${Constants.BINANCE_WEB_SOCKET_BASE_URL}${baseSymbol.lowercase() + quoteSymbol.lowercase()}$webSocketType$param")
                .build()
        return providesOkhttpClient.newWebSocket(request, providesBinanceWebSocketListener)
    }

    fun getBinanceSocketListener() = providesBinanceWebSocketListener
}