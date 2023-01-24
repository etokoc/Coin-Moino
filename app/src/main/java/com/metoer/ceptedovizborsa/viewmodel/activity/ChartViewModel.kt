package com.metoer.ceptedovizborsa.viewmodel.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.Ticker.CoinTickerResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceWebSocketCandleRoot
import com.metoer.ceptedovizborsa.data.response.coin.candles.CandlesData
import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinWebsocketTickerResponse
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {
    private var binanceSocketTickerLiveData: MutableLiveData<CoinWebsocketTickerResponse?>? = null
    private val tickerFromBinanceLiveData = MutableLiveData<CoinTickerResponse?>()

    var binanceSocketChartLiveData: MutableLiveData<BinanceWebSocketCandleRoot?>? = null
    val coinCanslesData = MutableLiveData<List<CandlesData>>()

    fun getAllCandlesData(
        interval: String,
        baseId: String,
        quetoId: String
    ) {
        repository.getAllCoinCandlesDataFromApi(
            CreateApiKeyUtil.getKey(),
            interval,
            baseId,
            quetoId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                response.data?.let {
                    coinCanslesData.value = it
                }

            }, {

            }).let {

            }
    }

    val chartBinanceLiveData = MutableLiveData<BinanceRoot>()
    fun getChartFromBinanceData(
        symbol: String,
        quote: String,
        interval: String
    ) {
        repository.getChartFromBinanceApi(symbol + quote, interval).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                chartBinanceLiveData.value = it
            }, {

            }).let {
            }
    }


    fun getTickerFromBinanceData(
        symbol: String,
        quote: String,
        windowSize: String,
    ): MutableLiveData<CoinTickerResponse?> {
        repository.getTickerFromBinanceApi(symbol + quote, windowSize).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tickerFromBinanceLiveData.value = it
            }, {

            }).let {

            }
        return tickerFromBinanceLiveData
    }

    fun clearGetTickerFromBinanceLiveData() {
        tickerFromBinanceLiveData.value = null
    }

    fun getBinanceTickerWebSocket(
        baseSymbol: String,
        quoteSymbol: String,
        webSocketType: String = "@ticker_",
        param: String = "1d"
    ): WebSocket {
        return repository.getBinanceTickerSocket(baseSymbol, quoteSymbol, webSocketType, param)
    }

    fun getBinanceSocketTickerListener(): MutableLiveData<CoinWebsocketTickerResponse?>? {
        binanceSocketTickerLiveData = repository.getBinanceSocketTickerListener().getData()
        return binanceSocketTickerLiveData
    }

    fun clearBinanceSocketTickerLiveData() {
        binanceSocketTickerLiveData?.value = null
    }

    fun getBinanceChartWebSocket(
        baseSymbol: String,
        quoteSymbol: String,
        webSocketType: String = "@kline_",
        param: String = "15m"
    ): WebSocket {
        return repository.getBinanceChartSocket(baseSymbol, quoteSymbol, webSocketType, param)
    }

    fun getBinanceSocketChartListener(): MutableLiveData<BinanceWebSocketCandleRoot?>? {
        binanceSocketChartLiveData = repository.getBinanceSocketChartListener().getData()
        return binanceSocketChartLiveData
    }

    fun clearBinanceSocketChartLiveData() {
        binanceSocketChartLiveData?.value = null
    }
}