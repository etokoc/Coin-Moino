package com.metoer.ceptedovizborsa.data.response.coin.candles


import com.google.gson.annotations.SerializedName

data class CandlesData(
    @SerializedName("close")
    var close: String?,
    @SerializedName("high")
    var high: String?,
    @SerializedName("low")
    var low: String?,
    @SerializedName("open")
    var open: String?,
    @SerializedName("period")
    var period: Long?,
    @SerializedName("volume")
    var volume: String?
)