package com.metoer.ceptedovizborsa.data.response.coin.candles

import com.google.gson.annotations.SerializedName

data class BinanceWebSocketCandleResponse(
    @SerializedName("t") var openTime:Long? = null,
    @SerializedName("T") var T: Int? = null,
    @SerializedName("s") var s: String? = null,
    @SerializedName("i") var i: String? = null,
    @SerializedName("f") var f: Int? = null,
    @SerializedName("L") var L: Int? = null,
    @SerializedName("o") var openPrice: String? = null,
    @SerializedName("c") var closePrice: String? = null,
    @SerializedName("h") var highPrice: String? = null,
    @SerializedName("l") var lowPrice: String? = null,
    @SerializedName("v") var _v: String? = null,
    @SerializedName("n") var n: Int? = null,
    @SerializedName("x") var x: Boolean? = null,
    @SerializedName("q") var _q: String? = null,
    @SerializedName("V") var V: String? = null,
    @SerializedName("Q") var Q: String? = null,
    @SerializedName("B") var B: String? = null
)