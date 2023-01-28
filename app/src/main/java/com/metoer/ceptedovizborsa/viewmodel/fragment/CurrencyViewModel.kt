package com.metoer.ceptedovizborsa.viewmodel.fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.CurrencyListSingleton
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.util.CreateApiKeyUtil
import com.metoer.ceptedovizborsa.util.SharedPrefencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {

    var currencyMutableList = MutableLiveData<List<Currency>>()
    var ratesMutableList = MutableLiveData<List<RatesData>>()

    fun getAllCurrencyData(timeUnix: String) {
        CurrencyListSingleton.clearMemory()
        currencyRepository.getCurrencyDataFromApi(timeUnix)
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
            }, {

            }).let {

            }
    }

    fun getAllRatesData(context: Context) {
        val list = ArrayList<RatesData>()
        val prefs = SharedPrefencesUtil(context)
        var language = prefs.getLocal("My_Lang", String)
        language = if (language.toString().isNullOrEmpty()) {
            "tr"
        } else {
            language
        }
        val uk = Locale(language.toString())
        currencyRepository.getRatesDataFromApi(CreateApiKeyUtil.getKey())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.data.forEachIndexed { index, ratesData ->
                    if (ratesData.type == "fiat" && ratesData.symbol != "SDG" && ratesData.symbol != "MRU"
                        && ratesData.symbol != "STN" && ratesData.symbol != "XPT" && ratesData.symbol != "CUC"
                        && ratesData.symbol != "XDR" && ratesData.symbol != "CLF" && ratesData.symbol != "CNH"
                        && ratesData.symbol != "VES" && ratesData.symbol != "ZWL" && ratesData.symbol != "SSB"
                        && ratesData.symbol != "SSP" && ratesData.symbol != "XPD"
                    ) {
                        val currency = java.util.Currency.getInstance(ratesData.symbol)
                        it.data[index].apply {
                            this.id = currency.getDisplayName(uk)
                        }
                        list.add(ratesData)
                    }
                    if (ratesData.symbol == "TRY")
                        it.data[index].apply {
                            turkishValue = 1 / (this.rateUsd?.toDouble() ?: 0.0)
                        }
                }
                ratesMutableList.postValue(list)
            }, {

            }).let {

            }
    }

    companion object {
        var turkishValue: Double = 0.0
    }
}