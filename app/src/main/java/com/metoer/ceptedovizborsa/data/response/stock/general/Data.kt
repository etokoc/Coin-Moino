package com.metoer.ceptedovizborsa.data.response.stock.general


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("ad")
    var ad: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("kod")
    var kod: String,
    @SerializedName("tip")
    var tip: String
)