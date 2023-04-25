package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.coin.Ticker.CoinTickerResponse
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesResponse
import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinPageTickerItem
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.Constants.AUTHORIZATIN_HEADER
import com.metoer.ceptedovizborsa.util.Constants.AUTHORIZATION
import com.metoer.ceptedovizborsa.util.Constants.BINANCE_CHART_BASE_URL
import com.metoer.ceptedovizborsa.util.Constants.CHART_BINANCE_ENDPOINT
import com.metoer.ceptedovizborsa.util.Constants.INTERVAL_QUERY
import com.metoer.ceptedovizborsa.util.Constants.LIMIT_100
import com.metoer.ceptedovizborsa.util.Constants.LIMIT_QUERY
import com.metoer.ceptedovizborsa.util.Constants.PAGE_TICKER_URL
import com.metoer.ceptedovizborsa.util.Constants.SYMBOL_QUERY
import com.metoer.ceptedovizborsa.util.Constants.TICKER_ENDPOINT
import com.metoer.ceptedovizborsa.util.Constants.TICKER_WINDOWSSIZE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface AppApi {

    @GET(Constants.RATES_BASE_URL)
    @QualifiedTypeConverterFactory.Json
    fun getAllRatesData(
        @Header(AUTHORIZATIN_HEADER) apiKey: String
    ): Observable<RatesResponse>

    @GET(Constants.COIN_MARKET_URL + LIMIT_100)
    @QualifiedTypeConverterFactory.Json
    @Headers(AUTHORIZATION)
    suspend fun getAllCoinData(@Query("offset") nextPageNumber: Int): CoinResponse

    /***
     * Binance Data
     */
//    https://api.binance.com/api/v3/uiKlines?symbol=BTCUSDT&interval=1M&limit=1000
    @GET("${BINANCE_CHART_BASE_URL}${CHART_BINANCE_ENDPOINT}")
    @QualifiedTypeConverterFactory.Json
    fun getChartFromBinanceData(
        @Query(SYMBOL_QUERY) symbol: String,
        @Query(INTERVAL_QUERY) interval: String,
        @Query(LIMIT_QUERY) limit: Int
    ): Observable<BinanceRoot>

    @GET("${BINANCE_CHART_BASE_URL}${TICKER_ENDPOINT}")
    @QualifiedTypeConverterFactory.Json
    fun getTickerFromBinanceData(
        @Query(SYMBOL_QUERY) symbol: String,
        @Query(TICKER_WINDOWSSIZE) windowSize: String,
    ): Observable<CoinTickerResponse>

    @GET("${BINANCE_CHART_BASE_URL}${PAGE_TICKER_URL}")
    @QualifiedTypeConverterFactory.Json
    fun getPageTickerData(): Observable<List<CoinPageTickerItem>>

}