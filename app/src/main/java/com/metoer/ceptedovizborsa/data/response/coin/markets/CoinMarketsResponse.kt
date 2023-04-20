package com.metoer.ceptedovizborsa.data.response.coin.markets


import com.google.gson.annotations.SerializedName

data class CoinMarketsResponse(
    @SerializedName("data")
    var data: List<PageTickerItem>?,
    @SerializedName("timestamp")
    var timestamp: Long
)