package com.metoer.ceptedovizborsa.util

import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.currency.Currency

class SortListUtil(

) {
    var arrayListForCurrency = listOf<Currency>()
    fun sortedForCurrencyList(
        itemlist: List<Currency>,
        sortedType: ListSortEnum,
        sortedByItem: ListSortEnum
    ): List<Currency> {
        if (sortedByItem == ListSortEnum.NAME)
            arrayListForCurrency = itemlist.sortedBy { it.Isim }
        if (sortedByItem == ListSortEnum.VALUE)
            arrayListForCurrency = itemlist.sortedBy { it.ForexBuying }

        if (sortedType == ListSortEnum.DESC) {
            arrayListForCurrency = arrayListForCurrency.reversed()
        }
        return arrayListForCurrency
    }

    var arrayListForCoin = listOf<CoinData>()
    fun sortedForCoinList(
        itemlist: List<CoinData>,
        sortedType: FilterEnum,
        sortedByItem: FilterEnum
    ): List<CoinData> {
        if (sortedByItem == FilterEnum.NAME)
            arrayListForCoin = itemlist.sortedBy { it.symbol }
        if (sortedByItem == FilterEnum.VOLUME)
            arrayListForCoin = itemlist.sortedBy { it.volumeUsd24Hr?.toDouble() }
        if (sortedByItem == FilterEnum.PRICE)
            arrayListForCoin = itemlist.sortedBy { it.priceUsd?.toDouble() }
        if (sortedByItem == FilterEnum.HOUR24)
            arrayListForCoin = itemlist.sortedBy { it.changePercent24Hr?.toDouble() }

        if (FilterEnum.ASC == sortedType) {
            arrayListForCoin = arrayListForCoin.reversed()
        }
        return arrayListForCoin
    }
}