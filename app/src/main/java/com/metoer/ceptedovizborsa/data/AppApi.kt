package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.Response.TarihDate
import com.metoer.ceptedovizborsa.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {
    @GET("today.xml")
    suspend fun getData(@Query(Constants.HEADER_DATA) timeUnix: String): Response<TarihDate>
}