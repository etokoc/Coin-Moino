package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.markets.CoinWebSocketResponse
import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinPageTickerItem
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import com.metoer.ceptedovizborsa.util.PageTickerTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class CoinPageViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {
    val coinLiveMarketCoinData = MutableLiveData<List<com.metoer.ceptedovizborsa.data.response.coin.markets.PageTickerItem>?>()
    var binanceSocketLiveData = MutableLiveData<CoinWebSocketResponse?>()
    fun getAllMarketsCoinData(
        quoteSymbol: String
    ): MutableLiveData<List<com.metoer.ceptedovizborsa.data.response.coin.markets.PageTickerItem>?> {
        currencyRepository.getAllMarketsCoinDataFromApi(CreateApiKeyUtil.getKey(), quoteSymbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response != null && !response.data.isNullOrEmpty())
                    coinLiveMarketCoinData.value = response.data
            }, {

            }).let {

            }
        return coinLiveMarketCoinData
    }

    fun getPageTickerData(enum: PageTickerTypeEnum): MutableLiveData<List<CoinPageTickerItem>> {
        val pageTickerLiveData = MutableLiveData<List<CoinPageTickerItem>>()
        currencyRepository.getPageTickerDataFromBinanceApi().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({response->
                pageTickerLiveData.value = response.filter {
                    it.symbol?.endsWith(enum.name) == true
                }
            }, {

            }).let {

            }
        return pageTickerLiveData
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
        binanceSocketLiveData.value = null
    }
}