package com.metoer.ceptedovizborsa.util

import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import com.metoer.ceptedovizborsa.data.response.currency.Currency

class SortListUtil(

) {
    var arrayListForCurrency = listOf<RatesData>()
    fun sortedForCurrencyList(
        itemlist: List<RatesData>,
        sortedType: ListSortEnum,
        sortedByItem: ListSortEnum
    ): List<RatesData> {
        if (sortedByItem == ListSortEnum.NAME)
            arrayListForCurrency = itemlist.sortedBy { it.id }
        if (sortedByItem == ListSortEnum.VALUE)
            arrayListForCurrency = itemlist.sortedBy { it.rateUsd?.toDouble() }

        if (sortedType == ListSortEnum.DESC) {
            arrayListForCurrency = arrayListForCurrency.reversed()
        }
        return arrayListForCurrency
    }

    fun<T> sortedForCoinList(
        itemlist: List<T>,
        sortedType: FilterEnum,
        sortedByItem: FilterEnum
    ): List<T> {
        var arrayListForCoin = listOf<T>()
        if (sortedByItem == FilterEnum.NAME)
            arrayListForCoin = itemlist.sortedBy {if (it is CoinData) it.symbol else (it as MarketData).baseSymbol  }
        if (sortedByItem == FilterEnum.VOLUME)
            arrayListForCoin = itemlist.sortedBy {if (it is CoinData) it.volumeUsd24Hr?.toDouble() else (it as MarketData).volumeUsd24Hr?.toDouble() }
        if (sortedByItem == FilterEnum.PRICE)
            arrayListForCoin = itemlist.sortedBy {if (it is CoinData) it.priceUsd?.toDouble() else (it as MarketData).priceUsd?.toDouble() }
        if (sortedByItem == FilterEnum.HOUR24)
            arrayListForCoin = itemlist.sortedBy {if (it is CoinData) it.changePercent24Hr?.toDouble() else (it as MarketData).percentExchangeVolume?.toDouble() }

        if (FilterEnum.ASC == sortedType) {
            arrayListForCoin = arrayListForCoin.reversed()
        }
        return arrayListForCoin
    }
}