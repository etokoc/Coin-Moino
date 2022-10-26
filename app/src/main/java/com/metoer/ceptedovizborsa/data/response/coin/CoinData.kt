package com.metoer.ceptedovizborsa.data.response.coin


import com.google.gson.annotations.SerializedName

data class CoinData(
    @SerializedName("changePercent24Hr")
    var changePercent24Hr: String?,
    @SerializedName("explorer")
    var explorer: String?,
    @SerializedName("id")
    var id: String,
    @SerializedName("marketCapUsd")
    var marketCapUsd: String?,
    @SerializedName("maxSupply")
    var maxSupply: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("priceUsd")
    var priceUsd: String?,
    @SerializedName("rank")
    var rank: String?,
    @SerializedName("supply")
    var supply: String?,
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("volumeUsd24Hr")
    var volumeUsd24Hr: String?,
    @SerializedName("vwap24Hr")
    var vwap24Hr: String?
)