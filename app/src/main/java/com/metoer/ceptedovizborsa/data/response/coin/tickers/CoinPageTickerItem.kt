package com.metoer.ceptedovizborsa.data.response.coin.tickers

import com.google.gson.annotations.SerializedName

data class CoinPageTickerItem(
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("priceChange") var priceChange: String? = null,
    @SerializedName("priceChangePercent") var priceChangePercent: String? = null,
    @SerializedName("lastPrice") var lastPrice: String? = null,
    @SerializedName("volume") var volume: String? = null,
    @SerializedName("quoteVolume") var quoteVolume: String? = null
):java.io.Serializable

