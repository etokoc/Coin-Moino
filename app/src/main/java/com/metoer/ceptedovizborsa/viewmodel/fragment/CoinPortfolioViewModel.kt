package com.metoer.ceptedovizborsa.viewmodel.fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.repository.CurrencyRepository
import com.metoer.ceptedovizborsa.data.repository.RoomRepository
import com.metoer.ceptedovizborsa.data.response.coin.avarage.CoinCurrentAvaragePriceItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CoinPortfolioViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    private var currentAvaragePriceLiveData: MutableLiveData<CoinCurrentAvaragePriceItem>? = null
    fun gelAllCoinBuyData(): MutableLiveData<List<CoinBuyItem>> {
        val data = MutableLiveData<List<CoinBuyItem>>()
        roomRepository.getAllCoinItems().subscribeOn(Schedulers.io())
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

    fun getCurrentAvaragePriceData(symbol: String): MutableLiveData<CoinCurrentAvaragePriceItem> {
        currentAvaragePriceLiveData = MutableLiveData<CoinCurrentAvaragePriceItem>()
        currencyRepository.getCurrentAvaragePriceDataFromApi(symbol).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                currentAvaragePriceLiveData!!.value = it
            }, {

            }).let {
                compositeDisposable.add(it)
            }
        return currentAvaragePriceLiveData!!
    }

    fun upsertCoinBuyItem(item: CoinBuyItem) {
        roomRepository.updateAdd(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun delete(item: CoinBuyItem) {
        roomRepository.delete(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun removeObserver(viewLifecycleOwner: LifecycleOwner) {
        currentAvaragePriceLiveData!!.value = null
        currentAvaragePriceLiveData!!.removeObservers(viewLifecycleOwner)
    }
}