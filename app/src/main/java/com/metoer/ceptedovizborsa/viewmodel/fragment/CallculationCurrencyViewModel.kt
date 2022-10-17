package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.CurrencyListSingleton
import com.metoer.ceptedovizborsa.data.response.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallculationCurrencyViewModel @Inject constructor() : ViewModel() {
    val currencyLiveData = MutableLiveData<ArrayList<Currency>>()

    fun getSpinnerList() {
        currencyLiveData.postValue(CurrencyListSingleton.getList())
    }
}