package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.TarihDate
import com.metoer.ceptedovizborsa.util.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {
    @GET("today.xml")
    fun getData(@Query(Constants.HEADER_DATA) timeUnix: String): Observable<TarihDate>
}