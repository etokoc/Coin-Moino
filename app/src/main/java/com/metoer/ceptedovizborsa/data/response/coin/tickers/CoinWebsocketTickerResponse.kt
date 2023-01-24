package com.metoer.ceptedovizborsa.data.response.coin.tickers

import com.google.gson.annotations.SerializedName

data class CoinWebsocketTickerResponse(
    @SerializedName("e") var e: String? = null,
    @SerializedName("E") var E: Int? = null,
    @SerializedName("s") var s: String? = null,
    @SerializedName("p") var _p: String? = null,
    @SerializedName("P") var priceChangePercent: String? = null,
    @SerializedName("w") var w: String? = null,
    @SerializedName("o") var o: String? = null,
    @SerializedName("h") var highPrice: String? = null,
    @SerializedName("l") var lowPrice: String? = null,
    @SerializedName("c") var lastPrice: String? = null,
    @SerializedName("v") var baseVolume: String? = null,
    @SerializedName("q") var quoteVolume: String? = null,
    @SerializedName("O") var O: Int? = null,
    @SerializedName("C") var C: Int? = null,
    @SerializedName("F") var F: Int? = null,
    @SerializedName("L") var L: Int? = null,
    @SerializedName("n") var n: Int? = null
)