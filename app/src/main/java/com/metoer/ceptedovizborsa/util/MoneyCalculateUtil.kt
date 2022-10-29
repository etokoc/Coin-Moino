package com.metoer.ceptedovizborsa.util

import com.metoer.ceptedovizborsa.data.response.currency.Currency
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object MoneyCalculateUtil {
    fun moneyConverter(
        currencyList: ArrayList<Currency>,
        money: Double,
        spinner1Position: Int,
        spinner2Position: Int
    ): String {
        val result =
            ((currencyList[spinner1Position].ForexBuying!! / currencyList[spinner1Position].Unit!!) * money) / (currencyList[spinner2Position].ForexBuying!! / currencyList[spinner2Position].Unit!!)
        return DecimalFormat("##.####").format(result).toString()
    }

    fun doubleConverter(p0: CharSequence): Double {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        val number: Number = format.parse(p0.toString()) as Number
        val d = number.toDouble()
        return d
    }
    fun volumeShortConverter(value: Double): String {
        var simplfy = ""
        if ((value / 1000000000) >= 1) {
            simplfy =
                "Hacim " + DecimalFormat("0.##").format(value / 1000000000) + " milyar"
        } else if ((value / 1000000) >= 1) {
            simplfy =
                "Hacim " + DecimalFormat("0.##").format(value / 1000000) + " milyon"
        } else {
            simplfy =
                "Hacim " + DecimalFormat("0.##").format(value)
        }
        return simplfy
    }
}