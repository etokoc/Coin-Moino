package com.metoer.ceptedovizborsa.data.response.coin.candles


import com.google.gson.annotations.SerializedName

data class CoinCandlesResponse(
    @SerializedName("data")
    var data: List<CandlesData>?,
    @SerializedName("timestamp")
    var timestamp: Long?
)