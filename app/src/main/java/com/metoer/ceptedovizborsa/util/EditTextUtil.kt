package com.metoer.ceptedovizborsa.util

import android.text.Editable
import android.text.InputFilter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object EditTextUtil {
    fun editTextFilter(): Array<out InputFilter> {
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            source.toString().replace('.', ',')
            return@InputFilter source
        }
        return arrayOf(inputFilter)
    }

    private fun selectSeperator(): String {
        val format = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
        return symbols.decimalSeparator.toString()
    }

    fun editTextSelectDigits(p0: Editable?): String {
        val resultString = if (p0.toString().contains(selectSeperator())) {
            "0123456789"
        } else {
            "0123456789" + selectSeperator()
        }
        return resultString
    }
}