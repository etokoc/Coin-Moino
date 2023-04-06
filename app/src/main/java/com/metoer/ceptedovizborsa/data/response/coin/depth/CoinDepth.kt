package com.metoer.ceptedovizborsa.data.response.coin.depth

import com.google.gson.annotations.SerializedName

data class CoinDepth(
    @SerializedName("lastUpdateId")
    var lastUpdateId: Int? = null,
    @SerializedName("bids")
    var bids: ArrayList<ArrayList<String>> = arrayListOf(),
    @SerializedName("asks")
    var asks: ArrayList<ArrayList<String>> = arrayListOf()
)