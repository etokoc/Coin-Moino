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
        CurrencyListSingleton.clearMemory()
        currencyRepository.getDataFromApi(timeUnix)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                val currencyResponse = it.Currency
                currencyResponse?.forEachIndexed { index, currency ->
                    if (currency.CurrencyCode == "XDR")
                        currencyResponse[index].apply {
                            this.Isim = "TÜRK LİRASI"
                            this.CurrencyName = "TÜRK LİRASI"
                            this.ForexBuying = 1.0
                            this.CurrencyCode = "TRY"
                            this.Kod = "TRY"
                            this.Kod = "TRY"
                        }
                }
                currencyMutableList.postValue(it.Currency)
                it.Currency?.let { currencyList ->
                    CurrencyListSingleton.setList(currencyList as ArrayList<Currency>)
                }
            }, {

            }, {

            }).let {

            }
    }
}