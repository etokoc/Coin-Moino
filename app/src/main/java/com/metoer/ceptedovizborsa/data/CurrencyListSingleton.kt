package com.metoer.ceptedovizborsa.data

import com.metoer.ceptedovizborsa.data.response.currency.Currency
import javax.inject.Singleton

@Singleton
object CurrencyListSingleton {
        private var currencyList: ArrayList<Currency>? = null
    init {
        currencyList = ArrayList()
    }
        fun setList(currencyList: ArrayList<Currency>) {
            this.currencyList!!.addAll(currencyList)
        }

        fun getList() = currencyList
        fun clearMemory() = currencyList!!.clear()

}

