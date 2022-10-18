package com.metoer.ceptedovizborsa.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.data.response.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCallculationCurrencyBinding
import com.metoer.ceptedovizborsa.util.showToastShort
import com.metoer.ceptedovizborsa.viewmodel.fragment.CallculationCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_callculation_currency.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CallculationCurrencyFragment : Fragment() {
    private val viewmodel: CallculationCurrencyViewModel by viewModels()
    private var _binding: FragmentCallculationCurrencyBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallculationCurrencyBinding.inflate(inflater, container, false)
        binding.apply {

        }

        return binding.root
    }

    private var currencyList = ArrayList<Currency>()
    override fun onResume() {
        super.onResume()
        viewmodel.getSpinnerList()
        initListener()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        viewmodel.currencyLiveData.observe(viewLifecycleOwner) {
            currencyList.addAll(it)
            initSpinners(currencyList)
        }

        var money = 0.0
        binding.apply {
            var editControl = false
            var editControl2 = false
            monayValueEditText1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty()) {
                        if (editControl && editControl2 == false) {
                            money = doubleConverter(p0)
                            moneyConverter(money, binding.monayValueEditText2, 1)
                        }
                    }
                    else{
                        monayValueEditText2.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    editControl = false
                }

            })

            monayValueEditText2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl2 = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty()) {
                        if (editControl2 == true && editControl == false) {
                            money = doubleConverter(p0)
                            moneyConverter(money, binding.monayValueEditText1, 2)
                        }
                    }
                    else {
                        monayValueEditText1.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    editControl2 = false

                }

            })

        }

    }

    fun doubleConverter(p0: CharSequence): Double {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        val number: Number = format.parse(p0.toString()) as Number
        val d = number.toDouble()
        return d
    }

    fun moneyConverter(money: Double, editText: EditText, firstVote: Int) {
        val money1position = moneyValueSpinner1.selectedItemPosition
        val money2position = moneyValueSpinner2.selectedItemPosition
        if (firstVote == 1) {
            val result =
                ((currencyList[money1position].ForexBuying!! / currencyList[money1position].Unit!!.toDouble()) * money) / currencyList[money2position].ForexBuying!! / currencyList[money2position].Unit!!.toDouble()
            editText.setText(
                DecimalFormat("##.####").format(result).toString())
        }
        if (firstVote == 2) {
            val result =
                ((currencyList[money2position].ForexBuying!! / currencyList[money2position].Unit!!.toDouble()) * money) / currencyList[money1position].ForexBuying!! / currencyList[money1position].Unit!!.toDouble()
            editText.setText(
                DecimalFormat("##.####").format(result).toString()
            )
        }
    }

    private fun initSpinners(currencyList: ArrayList<Currency>) {
        val arrayAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currencyList.map { currency -> currency.Isim })
        binding.moneyValueSpinner1.adapter = arrayAdapter
        binding.moneyValueSpinner2.adapter = arrayAdapter

        binding.apply {
            moneyValueSpinner1.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View,
                        i: Int,
                        l: Long
                    ) {
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {
                        return
                    }
                }
            moneyValueSpinner2.onItemSelectedListener
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    return
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}