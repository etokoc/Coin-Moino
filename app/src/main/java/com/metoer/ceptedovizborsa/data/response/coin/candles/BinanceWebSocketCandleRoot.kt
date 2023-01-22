package com.metoer.ceptedovizborsa.data.response.coin.candles

import com.google.gson.annotations.SerializedName

data class BinanceWebSocketCandleRoot(
    @SerializedName("e") var e: String? = null,
    @SerializedName("E") var E: Int? = null,
    @SerializedName("s") var s: String? = null,
    @SerializedName("k") var k: BinanceWebSocketCandleResponse? = BinanceWebSocketCandleResponse()
)