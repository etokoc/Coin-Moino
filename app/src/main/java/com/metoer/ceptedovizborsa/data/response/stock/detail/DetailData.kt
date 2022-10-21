package com.metoer.ceptedovizborsa.data.response.stock.detail


import com.google.gson.annotations.SerializedName

data class DetailData(
    @SerializedName("ccI14")
    var ccI14: Any,
    @SerializedName("hisseYuzeysel")
    var hisseYuzeysel: List<HisseYuzeysel>,
    @SerializedName("mov10")
    var mov10: Any,
    @SerializedName("rsI14")
    var rsI14: Any,
    @SerializedName("stc_5_3")
    var stc53: Any
)