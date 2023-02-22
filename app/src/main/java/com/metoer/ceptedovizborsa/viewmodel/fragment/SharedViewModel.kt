package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.util.FilterEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    var coinList: MutableLiveData<String?>? = MutableLiveData()
    var filterStatus: MutableLiveData<Pair<FilterEnum, FilterEnum>?>? = MutableLiveData()

    fun clearFilterStatusLiveData() {
        filterStatus?.value = null
    }

    fun clearCoinListLiveData() {
        coinList?.value = null
    }
}