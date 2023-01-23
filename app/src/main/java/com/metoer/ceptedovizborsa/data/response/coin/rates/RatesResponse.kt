package com.metoer.ceptedovizborsa.data.response.coin.rates

import com.google.gson.annotations.SerializedName

data class RatesResponse(
    @SerializedName("data") var data: List<RatesData>,
    @SerializedName("timestamp") var timestamp: Long? = null
)
