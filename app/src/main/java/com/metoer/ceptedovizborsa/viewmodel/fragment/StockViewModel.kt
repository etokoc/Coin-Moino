package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.stock.detail.StockDetailResponse
import com.metoer.ceptedovizborsa.data.response.stock.general.StockGeneralResponse
import com.metoer.ceptedovizborsa.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) : ViewModel() {
    val stockGeneralLiveData = MutableLiveData<List<StockGeneralResponse>>()
    val stockDetailLiveData = MutableLiveData<List<StockDetailResponse>>()
    fun getStockGeneralData() {
        currencyRepository.getStockGeneralDataFromApi(Constants.STOCK_BASE_URL + "hisse/list")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { generalDataList ->
                    stockGeneralLiveData.value = generalDataList
                }, {

                }
            ).let {

            }
    }

    fun getStockDetailData(stockName: String) {
        currencyRepository.getStockDetailDataFromApi(Constants.STOCK_BASE_URL + "borsa/hisseyuzeysel/$stockName")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { detailDataList ->
                    stockDetailLiveData.value = detailDataList

                }, {

                }
            ).let {

            }
    }
}