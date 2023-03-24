package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {
    private val coinLiveCoinData = MutableLiveData<List<CoinData>>()
//    fun  getAllCoinData(): MutableLiveData<List<CoinData>> {
//        currencyRepository.getAllCoinDataFromApi()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                coinLiveCoinData.value = it.data
//            }, {
//
//            }).let {
//
//            }
//        return coinLiveCoinData
//    }

    fun getAllCoinData() = currencyRepository.getAllCoinDataFromApi()
}
