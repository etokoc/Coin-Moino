package com.metoer.ceptedovizborsa.data.response.coin.Ticker


import com.google.gson.annotations.SerializedName

data class CoinTickerResponse(
    @SerializedName("closeTime")
    var closeTime: Long,
    @SerializedName("count")
    var count: Int,
    @SerializedName("firstId")
    var firstId: Long,
    @SerializedName("highPrice")
    var highPrice: String,
    @SerializedName("lastId")
    var lastId: Long,
    @SerializedName("lastPrice")
    var lastPrice: String,
    @SerializedName("lowPrice")
    var lowPrice: String,
    @SerializedName("openPrice")
    var openPrice: String,
    @SerializedName("openTime")
    var openTime: Long,
    @SerializedName("priceChange")
    var priceChange: String,
    @SerializedName("priceChangePercent")
    var priceChangePercent: String,
    @SerializedName("quoteVolume")
    var quoteVolume: String,
    @SerializedName("symbol")
    var symbol: String,
    @SerializedName("volume")
    var volume: String,
    @SerializedName("weightedAvgPrice")
    var weightedAvgPrice: String
)