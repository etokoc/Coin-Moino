package com.metoer.ceptedovizborsa.data.response.coin.candles

import com.google.gson.annotations.SerializedName

data class CandlesBinanceData(
    @SerializedName("0")
    var startTime: Int? = null,
    @SerializedName("1")
    var open: String? = null,
    @SerializedName("2")
    var high: String? = null,
    @SerializedName("3")
    var low: String? = null,
    @SerializedName("4")
    var close: String? = null,
    @SerializedName("5")
    var volume: String? = null,
    @SerializedName("6")
    var closeTime: Int? = null,
    @SerializedName("7")
    var numberOfTreades: String? = null,
    @SerializedName("8")
    var baseVolume: Int? = null,
    @SerializedName("9")
    var quoteVolume: String? = null,
    @SerializedName("10")
    var unUsedIgnore: String? = null,
    @SerializedName("11")
    var unsis: String? = null
)

