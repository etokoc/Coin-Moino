package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.coin.CoinResponse
import com.metoer.ceptedovizborsa.data.response.currency.TarihDate
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface AppApi {
    @GET("today.xml")
    @QualifiedTypeConverterFactory.Xml
    fun getCurrencyData(@Query(Constants.HEADER_DATA) timeUnix: String): Observable<TarihDate>

    @GET("https://api.coincap.io/v2/assets")
    @QualifiedTypeConverterFactory.Json
    @Headers("Authorization: 9be33ccd-1b0b-4009-ac01-0aed7af968f6")
    fun getAllCoinData(): Observable<CoinResponse>
}