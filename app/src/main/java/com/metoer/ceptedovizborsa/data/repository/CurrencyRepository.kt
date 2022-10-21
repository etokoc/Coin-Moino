package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.AppApi
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val appApi: AppApi) {
    fun getCurrencyDataFromApi(timeUnix: String) = appApi.getCurrencyData(timeUnix)
    fun getStockGeneralDataFromApi(url:String) = appApi.getStockGeneralData(url)
    fun getStockDetailDataFromApi(url: String) = appApi.getStockDetailData(url)
}