package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CoinPortfolioViewModel @Inject constructor(private val repository: RoomRepository) {
    protected val compositeDisposable = CompositeDisposable()
    fun gelAllCoinBuyData(): MutableLiveData<List<CoinBuyItem>> {
        repository.getAllCoinItems().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {

                }
            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }
}