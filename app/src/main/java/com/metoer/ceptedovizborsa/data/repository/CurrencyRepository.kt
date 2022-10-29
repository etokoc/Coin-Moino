package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val appApi: AppApi) {
    fun getCurrencyDataFromApi(timeUnix: String) = appApi.getCurrencyData(timeUnix)
    fun getAllCoinDataFromApi() = appApi.getAllCoinData()
    fun getAllMarketsCoinDataFromApi(apiKey: String, quoteSymbol: String) =
        appApi.getAllMarketsCoinData(apiKey, quoteSymbol)

    fun getAllCoinCanslesDataFromApi(
        apiKey: String,
        interval: String,
        baseId: String,
        quetoId: String
    ) = appApi.getAllCandlesCoinData(apiKey, interval, baseId, quetoId)
}