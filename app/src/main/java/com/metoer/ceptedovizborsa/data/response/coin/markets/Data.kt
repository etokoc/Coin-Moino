package com.metoer.ceptedovizborsa.data.response.coin.markets


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("baseId")
    var baseId: String,
    @SerializedName("baseSymbol")
    var baseSymbol: String,
    @SerializedName("exchangeId")
    var exchangeId: String,
    @SerializedName("percentExchangeVolume")
    var percentExchangeVolume: String,
    @SerializedName("priceQuote")
    var priceQuote: String,
    @SerializedName("priceUsd")
    var priceUsd: String,
    @SerializedName("quoteId")
    var quoteId: String,
    @SerializedName("quoteSymbol")
    var quoteSymbol: String,
    @SerializedName("rank")
    var rank: String,
    @SerializedName("tradesCount24Hr")
    var tradesCount24Hr: String,
    @SerializedName("updated")
    var updated: Long,
    @SerializedName("volumeUsd24Hr")
    var volumeUsd24Hr: String
)