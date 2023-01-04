package com.metoer.ceptedovizborsa.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCallculationCurrencyBinding
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.EditTextUtil
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.util.MoneyCalculateUtil
import com.metoer.ceptedovizborsa.util.setDefaultKeyListener
import com.metoer.ceptedovizborsa.viewmodel.fragment.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_callculation_currency.*
import java.util.*


@AndroidEntryPoint
class CallculationCurrencyFragment : Fragment() {

    private var spinner1Position = 0
    private var spinner2Position = 0

    private val viewmodel: CurrencyViewModel by viewModels()
    private var _binding: FragmentCallculationCurrencyBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallculationCurrencyBinding.inflate(inflater, container, false)
        binding.apply {
            if (savedInstanceState != null) {
                spinner1Position = savedInstanceState.getInt(Constants.SPINNER1_STATE_KEY, 0)
                spinner2Position = savedInstanceState.getInt(Constants.SPINNER2_STATE_KEY, 0)
            }
        }

        return binding.root
    }

    private var currencyList = ArrayList<Currency>()
    override fun onResume() {
        super.onResume()
        val unixTime = System.currentTimeMillis()
        viewmodel.getAllCurrencyData(unixTime.toString())
        binding.apply {
            moneyValueEditText1.filters = editTextFilter()
            moneyValueEditText2.filters = editTextFilter()
            moneyValueEditText1.hint = requireContext().getString(R.string.para_1)
            moneyValueEditText2.hint = requireContext().getString(R.string.para_2)
        }
        initListener()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        viewmodel.currencyMutableList.observe(viewLifecycleOwner) {
            currencyList = it as ArrayList<Currency>
            initSpinners(currencyList)
        }
        var money: Double
        binding.apply {
            var editControl = false
            var editControl2 = false
            moneyValueEditText1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty() && !moneyValueEditText1.text.toString()
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
                            binding.moneyValueEditText2.setText(moneyCalculate)
                        }
                    } else {
                        moneyValueEditText1.text.clear()
                        moneyValueEditText2.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    moneyValueEditText1.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                    editControl = false
                }

            })

            moneyValueEditText2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl2 = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty() && !moneyValueEditText2.text.toString()
                            .startsWith(',')
                    ) {
                        if (editControl2 && !editControl) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                currencyList,
                                money,
                                moneyValueSpinner2.selectedItemPosition,
                                moneyValueSpinner1.selectedItemPosition
                            )
                            binding.moneyValueEditText1.setText(moneyCalculate)
                        }
                    } else {
                        moneyValueEditText2.text.clear()
                        moneyValueEditText1.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    moneyValueEditText2.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
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
                currencyList.map { currency ->
                    if (currency.Kod!!.trim().lowercase() != "try") {
                        val resId: Int = resources.getIdentifier(
                            currency.Kod!!.trim().lowercase(),
                            "string", requireContext().packageName
                        )
                        getString(resId)
                    } else {
                        getString(R.string.tr)
                    }
                })
        binding.moneyValueSpinner1.adapter = arrayAdapter
        binding.moneyValueSpinner2.adapter = arrayAdapter
        binding.moneyValueSpinner1.setSelection(spinner1Position)
        binding.moneyValueSpinner2.setSelection(spinner2Position)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            Constants.SPINNER1_STATE_KEY,
            binding.moneyValueSpinner1.selectedItemPosition
        )
        outState.putInt(
            Constants.SPINNER2_STATE_KEY,
            binding.moneyValueSpinner2.selectedItemPosition
        )
    }

}