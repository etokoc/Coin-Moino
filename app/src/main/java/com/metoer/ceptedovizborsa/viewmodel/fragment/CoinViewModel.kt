package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.paging.AllCoinDataSoruce
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    appApi: AppApi
) :
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

    val getAllCoinData = Pager(config = PagingConfig(pageSize = 10)) {
        AllCoinDataSoruce(appApi)
    }.flow.cachedIn(viewModelScope)
}
