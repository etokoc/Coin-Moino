package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.CurrencyListSingleton
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {

    var currencyMutableList = MutableLiveData<List<Currency>>()

    fun getAllCurrencyData(timeUnix: String) {
        currencyRepository.getDataFromApi(timeUnix)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                currencyMutableList.postValue(it.Currency)
                it.Currency?.let { currencyList -> CurrencyListSingleton.setList(currencyList as ArrayList<Currency>) }
            }, {

            }, {

            }).let {

            }
    }
}