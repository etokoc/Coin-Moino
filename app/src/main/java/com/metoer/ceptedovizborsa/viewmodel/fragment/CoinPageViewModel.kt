package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CoinPageViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {
    private val coinLiveMarketCoinData = MutableLiveData<List<MarketData>?>()
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
}