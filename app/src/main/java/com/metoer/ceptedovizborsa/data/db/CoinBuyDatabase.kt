package com.metoer.ceptedovizborsa.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CoinBuyItem::class],
    version = 1
)
abstract class CoinBuyDatabase() : RoomDatabase() {
    abstract fun getCoinBuyDao(): CoinBuyDao
}
