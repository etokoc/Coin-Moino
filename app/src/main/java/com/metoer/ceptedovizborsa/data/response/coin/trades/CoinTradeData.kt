package com.metoer.ceptedovizborsa.data.response.coin.trades

import com.google.gson.annotations.SerializedName

data class CoinTradeData(
    @SerializedName("eventType") var e: String? = null,
    @SerializedName("E") var eventTime: Long? = null,
    @SerializedName("s") var symbol: String? = null,
    @SerializedName("t") var tradeId: Int? = null,
    @SerializedName("p") var price: String? = null,
    @SerializedName("q") var quantity: String? = null,
    @SerializedName("b") var b: Int? = null,
    @SerializedName("a") var a: Int? = null,
    @SerializedName("T") var tradeTime: Int? = null,
    @SerializedName("m") var buyerMarketMaker: Boolean? = null,
    @SerializedName("M") var M: Boolean? = null
)
