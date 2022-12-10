package com.metoer.ceptedovizborsa.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CoinBuyTable")
data class CoinBuyItem(
    @ColumnInfo(name = "coin_symbol")
    var coinSymbol: String? = null,
    @ColumnInfo(name = "coin_name")
    var coinName: String? = null,
    @ColumnInfo(name = "coin_unit")
    var coinUnit: Int? = null,
    @ColumnInfo(name = "coin_taked_value")
    var coinTakedValue: Double? = null,
    @ColumnInfo(name = "coin_taked_time")
    var coinTakedTime: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}