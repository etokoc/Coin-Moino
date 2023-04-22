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
    var binanceSocketLiveData = MutableLiveData<CoinWebSocketResponse?>()
    private val pageTickerLiveData = MutableLiveData<List<CoinPageTickerItem>>()

    companion object {
        private var pageTickerList = ArrayList<CoinPageTickerItem>()
    }

    fun getPageTickerData(enum: PageTickerTypeEnum? = null): MutableLiveData<List<CoinPageTickerItem>> {
        if (enum == null) {
            currencyRepository.getPageTickerDataFromBinanceApi().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                    pageTickerList.addAll(response)
                }, {

                }).let {

                }
        } else {
            pageTickerLiveData.value = pageTickerList.filter {
                it.symbol?.endsWith(enum.name) == true && ((it.volume?.toDouble()
                    ?: 0.0) > 0 && ((it.lastPrice?.toDouble() ?: 0.0) > 0))
            }
        }
        return pageTickerLiveData
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