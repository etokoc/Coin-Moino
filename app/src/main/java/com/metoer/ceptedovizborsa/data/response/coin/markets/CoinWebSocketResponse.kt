package com.metoer.ceptedovizborsa.data.response.coin.markets

import com.google.gson.annotations.SerializedName

data class CoinWebSocketResponse(
    @SerializedName("e") var e: String? = null,
    @SerializedName("E") var _E: Long? = null,
    @SerializedName("s") var symbol: String? = null,
    @SerializedName("p") var priceChange: String? = null,
    @SerializedName("P") var priceChangePercent: String? = null,
    @SerializedName("w") var w: String? = null,
    @SerializedName("x") var x: String? = null,
    @SerializedName("c") var lastPrice: String? = null,
    @SerializedName("Q") var Q: String? = null,
    @SerializedName("b") var b: String? = null,
    @SerializedName("B") var _B: String? = null,
    @SerializedName("a") var a: String? = null,
    @SerializedName("A") var _A: String? = null,
    @SerializedName("o") var o: String? = null,
    @SerializedName("h") var h: String? = null,
    @SerializedName("l") var l: String? = null,
    @SerializedName("v") var baseVolume: String? = null,
    @SerializedName("q") var queteVolume: String? = null,
    @SerializedName("O") var _O: Long? = null,
    @SerializedName("C") var _C: Long? = null,
    @SerializedName("F") var _F: Long? = null,
    @SerializedName("L") var _L: Long? = null,
    @SerializedName("n") var n: Int? = null
)
