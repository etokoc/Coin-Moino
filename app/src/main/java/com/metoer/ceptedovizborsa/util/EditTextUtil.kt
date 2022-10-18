package com.metoer.ceptedovizborsa.util

import android.text.InputFilter

object EditTextUtil {
    fun editTextFilter(): Array<out InputFilter> {
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            source.toString().replace('.', ',')
            return@InputFilter source
        }
        return arrayOf(inputFilter)
    }
}