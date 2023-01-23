package com.metoer.ceptedovizborsa.data.response.coin.rates

import com.google.gson.annotations.SerializedName

data class RatesData(
    @SerializedName("id") var id: String? = null,
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("currencySymbol") var currencySymbol: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("rateUsd") var rateUsd: String? = null
)
