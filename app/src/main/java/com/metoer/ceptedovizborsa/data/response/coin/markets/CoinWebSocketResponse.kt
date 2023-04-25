package com.metoer.ceptedovizborsa.data.response.coin.markets

import com.google.gson.annotations.SerializedName

data class CoinWebSocketResponse(
    @SerializedName("s") var symbol: String? = null,
    @SerializedName("p") var priceChange: String? = null,
    @SerializedName("P") var priceChangePercent: String? = null,
    @SerializedName("c") var lastPrice: String? = null,
    @SerializedName("v") var baseVolume: String? = null,
    @SerializedName("q") var queteVolume: String? = null,
)
