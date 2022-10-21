package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.currency.TarihDate
import com.metoer.ceptedovizborsa.data.response.stock.detail.DetailData
import com.metoer.ceptedovizborsa.data.response.stock.general.StockGeneralResponse
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface AppApi {
    @GET("today.xml")
    @QualifiedTypeConverterFactory.Xml
    fun getCurrencyData(@Query(Constants.HEADER_DATA) timeUnix: String): Observable<TarihDate>

    @GET
    @QualifiedTypeConverterFactory.Json
    fun getStockGeneralData(@Url url: String): Observable<StockGeneralResponse>

    @GET
    @QualifiedTypeConverterFactory.Json
    fun getStockDetailData(@Url url: String): Observable<DetailData>


}