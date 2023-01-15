package com.metoer.ceptedovizborsa.viewmodel.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.Ticker.CoinTickerResponse
import com.metoer.ceptedovizborsa.data.response.coin.candles.*
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {
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
    ): MutableLiveData<CoinTickerResponse> {
        val data = MutableLiveData<CoinTickerResponse>()
        repository.getTickerFromBinanceApi(symbol + quote, windowSize).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                data.value = it
            }, {

            }).let {

            }
        return data
    }
}