package com.metoer.ceptedovizborsa.data.webscoket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinWebSocketResponse
import com.metoer.ceptedovizborsa.util.Constants.WEBSOCKET_CLOSE_NORMAL
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class BinanceWebSocketCoinListener : WebSocketListener() {

    companion object {
        var data: MutableLiveData<List<CoinWebSocketResponse>?>? = MutableLiveData()
        var isRunning = false
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        /**
         * send kullanımı websocket'e message body göndermek için kullanılır.
         */
//        webSocket.send("")
        Log.i("WEBSOCKET BinanceWebSocketCoinListener", "Websockete bağlandı (coin için)")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        isRunning = true
        writeToLog("Received: $text")
        if (text.isNotEmpty()) {
            val compressedData = compressData(text)
            val json = JsonParser.parseString(
                GZIPInputStream(ByteArrayInputStream(compressedData)).reader(Charsets.UTF_8)
                    .use { it.readText() })
            val coinWebSocketResponses =
                Gson().fromJson(json, Array<CoinWebSocketResponse>::class.java)
            val chunkedData = coinWebSocketResponses.chunked(1)

            for (chunk in chunkedData) {
                data?.postValue(chunk)
            }
            data?.postValue(coinWebSocketResponses.toList())
        } else {
            writeToLog("Received: text is null")
        }
    }

    private inline fun <reified T> Array<T>.chunked(size: Int): List<List<T>> {
        return this.toList().chunked(size)
    }

    private fun compressData(data: String): ByteArray {
        val outputStream = ByteArrayOutputStream(data.length)
        val gzip = GZIPOutputStream(outputStream)
        gzip.write(data.toByteArray(Charsets.UTF_8))
        gzip.close()
        return outputStream.toByteArray()
    }

    fun getIsrunning() = isRunning

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(WEBSOCKET_CLOSE_NORMAL, null)
        writeToLog("Closing: $code $reason")
        isRunning = false
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        writeToLog("Error : " + t.message)
    }

    private fun writeToLog(text: String) {
        Log.i("WEBSOCKET BinanceWebSocketCoinListener", text)
    }

    fun getData(): MutableLiveData<List<CoinWebSocketResponse>?>? {
        return if (data != null) {
            data
        } else
            null
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        isRunning = false
    }
}