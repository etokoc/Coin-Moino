package com.metoer.ceptedovizborsa.data.repository

import com.metoer.ceptedovizborsa.data.db.CoinBuyDao
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val db: CoinBuyDao
) {
    fun updateAdd(item: CoinBuyItem) = db.upsert(item)
    fun delete(item: CoinBuyItem) = db.delete(item)
    fun getAllCoinItems() = db.getAllCoinBuyData()
}