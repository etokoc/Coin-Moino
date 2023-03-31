package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.paging.AllCoinDataSoruce
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    appApi: AppApi
) :
    ViewModel() {
    val getAllCoinData = Pager(config = PagingConfig(pageSize = 10)) {
        AllCoinDataSoruce(appApi)
    }.flow.cachedIn(viewModelScope)
}
