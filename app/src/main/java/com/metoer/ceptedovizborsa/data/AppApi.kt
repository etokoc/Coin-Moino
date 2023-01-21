package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.candles.CoinCandlesResponse
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinMarketsResponse
import com.metoer.ceptedovizborsa.data.response.currency.TarihDate
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.Constants.AUTHORIZATIN_HEADER
import com.metoer.ceptedovizborsa.util.Constants.AUTHORIZATION
import com.metoer.ceptedovizborsa.util.Constants.BASE_ID_QUERY
import com.metoer.ceptedovizborsa.util.Constants.BINANCE_CHART_BASE_URL
import com.metoer.ceptedovizborsa.util.Constants.CANDLES_COIN_DATA_GET_ENDPOINT
import com.metoer.ceptedovizborsa.util.Constants.CHART_BINANCE_ENDPOINT
import com.metoer.ceptedovizborsa.util.Constants.INTERVAL_QUERY
import com.metoer.ceptedovizborsa.util.Constants.LIMIT_250
import com.metoer.ceptedovizborsa.util.Constants.LIMIT_QUERY
import com.metoer.ceptedovizborsa.util.Constants.MARKET_COIN_DATA
import com.metoer.ceptedovizborsa.util.Constants.QUERY_QUOTE_SYMBOL
import com.metoer.ceptedovizborsa.util.Constants.QUOTE_ID
import com.metoer.ceptedovizborsa.util.Constants.SYMBOL_QUERY
import com.metoer.ceptedovizborsa.util.Constants.TICKER_ENDPOINT
import com.metoer.ceptedovizborsa.util.Constants.TICKER_WINDOWSSIZE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface AppApi {
    @GET(Constants.TODAY_XML)
    @QualifiedTypeConverterFactory.Xml
    fun getCurrencyData(@Query(Constants.HEADER_DATA) timeUnix: String): Observable<TarihDate>

    @GET(Constants.COIN_MARKET_URL + LIMIT_250)//100
    @QualifiedTypeConverterFactory.Json
    @Headers(AUTHORIZATION)
    fun getAllCoinData(): Observable<CoinResponse>

    @GET("${Constants.COINCAP_BASE_URL}${MARKET_COIN_DATA}")
    @QualifiedTypeConverterFactory.Json
    fun getAllMarketsCoinData(
        @Header(AUTHORIZATIN_HEADER) apiKey: String,
        @Query(QUERY_QUOTE_SYMBOL) quoteSymbol: String
    ): Observable<CoinMarketsResponse>

    @GET("${Constants.COINCAP_BASE_URL}${CANDLES_COIN_DATA_GET_ENDPOINT}")
    @QualifiedTypeConverterFactory.Json
    fun getAllCandlesCoinData(
        @Header(AUTHORIZATIN_HEADER) apiKey: String,
        @Query(INTERVAL_QUERY) interval: String,
        @Query(BASE_ID_QUERY) baseId: String,
        @Query(QUOTE_ID) quoteId: String
    ): Observable<CoinCandlesResponse>

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
    ):Observable<BinanceRoot>
}