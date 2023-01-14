package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRootSubList
import com.metoer.ceptedovizborsa.data.response.coin.candles.CandlesBinaceRootResponse
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val appApi: AppApi) {
    fun getCurrencyDataFromApi(timeUnix: String) = appApi.getCurrencyData(timeUnix)
    fun getAllCoinDataFromApi() = appApi.getAllCoinData()
    fun getAllMarketsCoinDataFromApi(apiKey: String, quoteSymbol: String) =
        appApi.getAllMarketsCoinData(apiKey, quoteSymbol)
    fun getChartFromBinanceApi(symbol: String, interval: String, limit: Int = Constants.BINANCE_CHART_LIMIT): Observable<BinanceRoot> {
       return appApi.getChartFromBinanceData(symbol, interval, limit)
    }

    fun getAllCoinCandlesDataFromApi(
        apiKey: String,
        interval: String,
        baseId: String,
        quetoId: String
    ) = appApi.getAllCandlesCoinData(apiKey, interval, baseId, quetoId)
}