package com.metoer.ceptedovizborsa.data.response.stock.general


import com.google.gson.annotations.SerializedName

data class StockGeneralResponse(
    @SerializedName("code")
    var code: String,
    @SerializedName("data")
    var data: List<Data>
)