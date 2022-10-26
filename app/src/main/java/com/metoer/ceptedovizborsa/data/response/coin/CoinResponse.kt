package com.metoer.ceptedovizborsa.data.response.coin


import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("data")
    var data: List<Data>,
    @SerializedName("timestamp")
    var timestamp: Long
)