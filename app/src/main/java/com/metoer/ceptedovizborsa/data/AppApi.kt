package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.currency.TarihDate
import com.metoer.ceptedovizborsa.data.response.stock.detail.StockDetailResponse
import com.metoer.ceptedovizborsa.data.response.stock.general.StockGeneralResponse
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppApi {
    @GET("today.xml")
    @QualifiedTypeConverterFactory.Xml
    fun getCurrencyData(@Query(Constants.HEADER_DATA) timeUnix: String): Observable<TarihDate>

    @GET("hisse/list")
    @QualifiedTypeConverterFactory.Json
    fun getStockGeneralData(): Observable<StockGeneralResponse>

    @GET("borsa/hisseyuzeysel/{stockName}")
    @QualifiedTypeConverterFactory.Json
    fun getStockDetailData(@Path("stockName") stockName: String): Observable<StockDetailResponse>


}