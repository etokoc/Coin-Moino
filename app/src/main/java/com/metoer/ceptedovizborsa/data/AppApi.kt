package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.candles.CandlesBinaceRootResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.CoinCandlesResponse
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinMarketsResponse
import com.metoer.ceptedovizborsa.data.response.currency.TarihDate
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.Constants.BINANCE_CHART_BASE_URL
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface AppApi {
    @GET("today.xml")
    @QualifiedTypeConverterFactory.Xml
    fun getCurrencyData(@Query(Constants.HEADER_DATA) timeUnix: String): Observable<TarihDate>

    @GET(Constants.COIN_MARKET_URL + "?limit=250")//100
    @QualifiedTypeConverterFactory.Json
    @Headers("Authorization: 9be33ccd-1b0b-4009-ac01-0aed7af968f6")
    fun getAllCoinData(): Observable<CoinResponse>

    @GET("${Constants.COINCAP_BASE_URL}markets?&limit=200&exchangeId=binance")
    @QualifiedTypeConverterFactory.Json
    fun getAllMarketsCoinData(
        @Header("Authorization") apiKey: String,
        @Query("quoteSymbol") quoteSymbol: String
    ): Observable<CoinMarketsResponse>

    @GET("${Constants.COINCAP_BASE_URL}candles?exchange=binance")
    @QualifiedTypeConverterFactory.Json
    fun getAllCandlesCoinData(
        @Header("Authorization") apiKey: String,
        @Query("interval") interval: String,
        @Query("baseId") baseId: String,
        @Query("quoteId") quoteId: String
    ): Observable<CoinCandlesResponse>

    /***
     * Binance Data
     */
//    https://api.binance.com/api/v3/uiKlines?symbol=BTCUSDT&interval=1M&limit=1000
    @GET("${BINANCE_CHART_BASE_URL}api/v3/uiKlines?")
    @QualifiedTypeConverterFactory.Json
    fun getChartFromBinanceData(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int
    ):Observable<BinanceRoot>
}