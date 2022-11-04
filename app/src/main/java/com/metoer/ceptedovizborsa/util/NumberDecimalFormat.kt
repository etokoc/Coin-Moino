package com.metoer.ceptedovizborsa.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object NumberDecimalFormat {
    fun numberDecimalFormat(value: String,pattern:String): String {
        return DecimalFormat(
            pattern,
            DecimalFormatSymbols.getInstance()
        ).format(value.toDouble())
    }
}