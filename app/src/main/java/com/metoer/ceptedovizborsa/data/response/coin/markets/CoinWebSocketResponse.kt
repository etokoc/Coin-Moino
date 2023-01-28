package com.metoer.ceptedovizborsa.data.response.coin.markets

import com.google.gson.annotations.SerializedName

data class CoinWebSocketResponse(
    @SerializedName("exchange") var exchange: String? = null,
    @SerializedName("base") var base: String? = null,
    @SerializedName("quote") var quote: String? = null,
    @SerializedName("direction") var direction: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("volume") var volume: Double? = null,
    @SerializedName("timestamp") var timestamp: Int? = null,
    @SerializedName("priceUsd") var priceUsd: Double? = null
)
