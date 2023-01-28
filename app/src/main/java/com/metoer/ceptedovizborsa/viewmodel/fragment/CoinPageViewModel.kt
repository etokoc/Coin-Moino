package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceWebSocketCandleRoot
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinWebSocketResponse
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class CoinPageViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {
    val coinLiveMarketCoinData = MutableLiveData<List<MarketData>?>()
    var binanceSocketLiveData = MutableLiveData<CoinWebSocketResponse?>()
    fun getAllMarketsCoinData(
        quoteSymbol: String
    ): MutableLiveData<List<MarketData>?> {
        currencyRepository.getAllMarketsCoinDataFromApi(CreateApiKeyUtil.getKey(), quoteSymbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                coinLiveMarketCoinData.value = it.data
            }, {

            }).let {

            }
        return coinLiveMarketCoinData
    }

    fun clearAllMarketCoinData() {
        coinLiveMarketCoinData.value = null
    }


    fun getBinanceCoinWebSocket(): WebSocket {
        return currencyRepository.getBinanceCoinSocket()
    }

    fun getBinanceSocketListener(): MutableLiveData<CoinWebSocketResponse?> {
        binanceSocketLiveData = currencyRepository.getBinanceSocketListener().getData()!!
        return binanceSocketLiveData
    }

    fun clearBinanceSocketLiveData() {
        binanceSocketLiveData?.value = null
    }


}