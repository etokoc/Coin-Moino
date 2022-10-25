package com.metoer.ceptedovizborsa.util

import com.metoer.ceptedovizborsa.data.response.currency.Currency

class SortListUtil(
    val itemlist: List<Currency>,
    val sortedType: ListSortEnum,
    val sortedByItem: ListSortEnum
) {
    var arrayList = listOf<Currency>()
    fun sortedList(): List<Currency> {
        if (sortedByItem == ListSortEnum.NAME)
            arrayList = itemlist.sortedBy { it.Isim }
        if (sortedByItem == ListSortEnum.VALUE)
            arrayList = itemlist.sortedBy { it.ForexBuying }

        if (sortedType == ListSortEnum.DESC) {
            arrayList = arrayList.reversed()
        }
        return arrayList
    }
}