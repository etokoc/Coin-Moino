package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.Currency
import javax.inject.Singleton

@Singleton
object CurrencyListSingleton {
    private var currencyList = ArrayList<Currency>()

    fun setList(currencyList: List<Currency>) {
        this.currencyList.addAll(currencyList)
    }

    fun getList() = currencyList

    fun clearMemory() = currencyList.clear()
}