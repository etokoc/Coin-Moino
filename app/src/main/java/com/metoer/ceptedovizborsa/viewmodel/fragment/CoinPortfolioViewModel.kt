package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CoinPortfolioViewModel @Inject constructor(private val repository: RoomRepository):ViewModel() {
      val compositeDisposable = CompositeDisposable()

    fun gelAllCoinBuyData(): MutableLiveData<List<CoinBuyItem>> {
        val data = MutableLiveData<List<CoinBuyItem>>()
        repository.getAllCoinItems().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    data.value = it
                }
            }, {

            }).let {
                compositeDisposable.add(it)
            }
        return data
    }

    fun upsertCoinBuyItem(item: CoinBuyItem) {
        repository.updateAdd(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun delete(item: CoinBuyItem){
        repository.delete(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }
}