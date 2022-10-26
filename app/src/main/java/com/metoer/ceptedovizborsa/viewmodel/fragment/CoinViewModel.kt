package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) :
    ViewModel() {

}