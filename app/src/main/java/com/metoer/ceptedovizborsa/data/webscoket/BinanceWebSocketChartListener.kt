package com.metoer.ceptedovizborsa.data.webscoket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceWebSocketCandleRoot
import com.metoer.ceptedovizborsa.util.Constants
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class BinanceWebSocketChartListener : WebSocketListener() {

    companion object {
        var data: MutableLiveData<BinanceWebSocketCandleRoot?>? = MutableLiveData()
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        /**
         * send kullanımı websocket'e message body göndermek için kullanılır.
         */
//        webSocket.send("")
        Log.e("WEBSOCKET", "Websockete bağlandı candle")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        writeToLog("Received: $text")
        if (text.isNotEmpty()) {
            val json = JsonParser.parseString(text)
            data?.postValue(Gson().fromJson(json, BinanceWebSocketCandleRoot::class.java))
        } else {
            writeToLog("Received: text is null")
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(2022, null)
        writeToLog("Closing: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        writeToLog("Error : " + t.message)
    }

    private fun writeToLog(text: String) {
        Log.e("WEBSOCKET", "$text")
    }

    fun getData(): MutableLiveData<BinanceWebSocketCandleRoot?>? {
        return if (data != null) {
            data
        } else
            null
    }
}