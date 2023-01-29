package com.metoer.ceptedovizborsa.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object MoneyCalculateUtil {

    fun coinConverter(edittextUnit: EditText, edittextTotal: EditText, price: String) {
        var money: Double
        var editControl = false
        var editControl2 = false
        edittextUnit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                editControl = true
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.isNotEmpty() && !edittextUnit.text.toString()
                        .startsWith(',')
                ) {
                    if (editControl && !editControl2) {
                        money = doubleConverter(p0)
                        val coinCalculate = price.toDouble() * money
                        edittextTotal.setText(
                            DecimalFormat("##.######").format(coinCalculate).toString()
                        )
                    }
                } else {
                    edittextUnit.text?.clear()
                    edittextTotal.text?.clear()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                edittextUnit.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                editControl = false
            }

        })

        edittextTotal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                editControl2 = true
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.isNotEmpty() && !edittextTotal.text.toString()
                        .startsWith(',')
                ) {
                    if (editControl2 && !editControl) {
                        money = doubleConverter(p0)
                        val coinCalculate = money / price.toDouble()
                        edittextUnit.setText(
                            DecimalFormat("##.######").format(coinCalculate).toString()
                        )
                    }
                } else {
                    edittextTotal.text?.clear()
                    edittextUnit.text?.clear()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                edittextTotal.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                editControl2 = false
            }

        })
    }

    fun moneyConverter(
        money: Double,
        ratesData1: RatesData,
        ratesData2: RatesData
    ): String {
        val result =
            (ratesData1.rateUsd!!.toDouble()) * money / (ratesData2.rateUsd!!.toDouble())
        return DecimalFormat("##.####").format(result).toString()
    }

    fun doubleConverter(p0: CharSequence): Double {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        val number: Number = format.parse(p0.toString()) as Number
        val d = number.toDouble()
        return d
    }

    fun doubleConverter(p0: String): Double {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        val number: Number = format.parse(p0) as Number
        val d = number.toDouble()
        return d
    }

    fun volumeShortConverter(value: Double, context: Context): String {
        val simplfy: String
        if ((value / 1000000000000) >= 1) {
            simplfy = context.getString(
                R.string.hacim_calculate4,
                DecimalFormat("0.##").format(value / 1000000000000)
            )
        } else if ((value / 1000000000) >= 1) {
            simplfy = context.getString(
                R.string.hacim_calculate,
                DecimalFormat("0.##").format(value / 1000000000)
            )
        } else if ((value / 1000000) >= 1) {
            simplfy = context.getString(
                R.string.hacim_calculate2,
                DecimalFormat("0.##").format(value / 1000000)
            )
        } else {
            simplfy =
                context.getString(
                    R.string.hacim_calculate3,
                    DecimalFormat("###,###,###.##").format(value)
                )
        }
        return simplfy
    }
}