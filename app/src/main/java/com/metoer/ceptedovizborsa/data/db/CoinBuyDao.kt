package com.metoer.ceptedovizborsa.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
@Dao
interface CoinBuyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(item: CoinBuyItem): Completable

    @Delete
    fun delete(item: CoinBuyItem): Completable

    @Query("SELECT * FROM CoinBuyTable")
    fun getAllCoinBuyData(): Single<List<CoinBuyItem>>

}