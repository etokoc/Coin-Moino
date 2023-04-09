package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.depth.CoinDepth
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {

    private var binanceSocketDepthLiveData: MutableLiveData<CoinDepth?>? = null

    fun getBinanceDepthWebSocket(
        baseSymbol: String,
        quoteSymbol: String
    ): WebSocket {
        return repository.getBinanceDepthSocket(baseSymbol, quoteSymbol)
    }

    fun getBinanceSocketDepthListener(): MutableLiveData<CoinDepth?>? {
        binanceSocketDepthLiveData = repository.getBinanceSocketDepthListener().getData()
        return binanceSocketDepthLiveData
    }

    fun clearBinanceSocketDepthLiveData() {
        binanceSocketDepthLiveData?.value = null
    }
}