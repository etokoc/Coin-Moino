package com.metoer.ceptedovizborsa.data.response.coin.avarage

import com.google.gson.annotations.SerializedName

data class CoinCurrentAvaragePriceItem(
    @SerializedName("mins")
    var mins: Int,
    @SerializedName("price")
    var price: String
)