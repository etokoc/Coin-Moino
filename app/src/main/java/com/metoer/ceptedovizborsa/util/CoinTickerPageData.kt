package com.metoer.ceptedovizborsa.util

import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinPageTickerItem

object CoinTickerPageData {
    private val pageTickerList = ArrayList<CoinPageTickerItem>()

    fun setPageTickerList(response: List<CoinPageTickerItem>) {
        pageTickerList.clear()
        pageTickerList.addAll(response)
    }

    fun getPageTickerList(enum: PageTickerTypeEnum? = null): List<CoinPageTickerItem> {
        return if (enum == null) {
            pageTickerList
        } else {
            pageTickerList.filter {
                it.symbol?.endsWith(enum.name) == true && ((it.volume?.toDouble()
                    ?: 0.0) > 0 && ((it.lastPrice?.toDouble() ?: 0.0) > 0))
            }
        }
    }
}