package com.metoer.ceptedovizborsa.viewmodel.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.stock.detail.HisseYuzeysel
import com.metoer.ceptedovizborsa.data.response.stock.general.Data
import com.metoer.ceptedovizborsa.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {
    var stockGeneralLiveData = MutableLiveData<List<Data>>()
    var stockDetailLiveData = MutableLiveData<List<HisseYuzeysel>>()
    fun getStockGeneralData() {
        Log.i("valueConst", "getStockGeneralData: " + Constants.STOCK_BASE_URL)
        currencyRepository.getStockGeneralDataFromApi(Constants.STOCK_BASE_URL + "hisse/list")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { generalDataList ->
                    stockGeneralLiveData.value = generalDataList.data
                }, {
                    Log.i("error", "" + it)
                }
            ).let {

            }
    }

    /*fun getStockDetailData(stockName: String) {
        currencyRepository.getStockDetailDataFromApi(Constants.STOCK_BASE_URL + "borsa/hisseyuzeysel/$stockName")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { detailDataList ->
                    stockDetailLiveData.value = detailDataList.hisseYuzeysel

                }, {

                }
            ).let {

            }
    }*/
}