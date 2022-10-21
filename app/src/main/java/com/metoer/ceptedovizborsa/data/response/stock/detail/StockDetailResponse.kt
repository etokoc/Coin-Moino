package com.metoer.ceptedovizborsa.data.response.stock.detail


import com.google.gson.annotations.SerializedName

data class StockDetailResponse(
    @SerializedName("code")
    var code: String,
    @SerializedName("data")
    var data: DetailData
)