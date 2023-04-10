package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.trades.CoinTradeData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: CurrencyRepository) :
    ViewModel() {

    private var binanceSocketRatesData: MutableLiveData<CoinTradeData?>? = null

    fun getBinanceTradeWebSocket(baseSymbol: String, quoteSymbol: String): WebSocket {
        return repository.getBinanceTradeSocket(baseSymbol, quoteSymbol)
    }

    fun getBinanceSocketTradeListener(): MutableLiveData<CoinTradeData?>? {
        binanceSocketRatesData = repository.getBinanceSocketTradeListener().getData()
        return binanceSocketRatesData
    }

    fun clearBinanceSocketTradeLiveData() {
        binanceSocketRatesData?.value = null
    }
}