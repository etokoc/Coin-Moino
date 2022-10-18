package com.metoer.ceptedovizborsa.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.data.response.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCallculationCurrencyBinding
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.util.MoneyCalculateUtil
import com.metoer.ceptedovizborsa.util.setDefaultKeyListener
import com.metoer.ceptedovizborsa.viewmodel.fragment.CallculationCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_callculation_currency.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


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
        binding.monayValueEditText1.filters = editTextFilter()
        binding.monayValueEditText2.filters = editTextFilter()
        initListener()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        val format = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
        val defaultSeperator = symbols.decimalSeparator.toString()
        viewmodel.currencyLiveData.observe(viewLifecycleOwner) {
            currencyList.addAll(it)
            initSpinners(currencyList)
        }

        var money: Double
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
                    if (p0 != null && p0.isNotEmpty() && !binding.monayValueEditText1.text.toString()
                            .startsWith(',')
                    ) {
                        if (editControl && !editControl2) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                currencyList,
                                money,
                                moneyValueSpinner1.selectedItemPosition,
                                moneyValueSpinner2.selectedItemPosition
                            )
                            binding.monayValueEditText2.setText(moneyCalculate)
                        }
                    } else {
                        monayValueEditText1.text.clear()
                        monayValueEditText2.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().contains(defaultSeperator)) {
                        monayValueEditText1.setDefaultKeyListener("0123456789")
                    } else {
                        monayValueEditText1.setDefaultKeyListener("0123456789" + defaultSeperator)
                    }
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
                        if (editControl2 && !editControl && !binding.monayValueEditText1.text.toString()
                                .startsWith(',')
                        ) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                currencyList,
                                money,
                                moneyValueSpinner2.selectedItemPosition,
                                moneyValueSpinner1.selectedItemPosition
                            )
                            binding.monayValueEditText1.setText(moneyCalculate)
                        }
                    } else {
                        monayValueEditText2.text.clear()
                        monayValueEditText1.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().contains(defaultSeperator)) {
                        monayValueEditText2.setDefaultKeyListener("0123456789")
                    } else {
                        monayValueEditText2.setDefaultKeyListener("0123456789" + defaultSeperator)
                    }
                    editControl2 = false
                }

            })

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