package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.data.CurrencyListSingleton
import com.metoer.ceptedovizborsa.data.response.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCallculationCurrencyBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.CallculationCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


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


    private fun initListener() {
        viewmodel.currencyLiveData.observe(viewLifecycleOwner) {
            currencyList.addAll(it)
            initSpinners(currencyList)
        }

        var money = 0.0
        binding.apply {
            monayValueEditText1.addTextChangedListener {
//                money = if (it!!.isEmpty()) 0.0 else
//                    it.toString().toDouble()
//                val result =
//                    ((currencyList[moneyValueSpinner1.selectedItemPosition].ForexBuying!!) * money) / currencyList[moneyValueSpinner2.selectedItemPosition].ForexBuying!!
//                monayValueEditText2.setText(result.toString())
            }
            monayValueEditText2.addTextChangedListener {
                money = if (it!!.isEmpty()) 0.0 else
                    it.toString().toDouble()
                val money1position = moneyValueSpinner1.selectedItemPosition
                val money2position = moneyValueSpinner2.selectedItemPosition
                val result =
                    ((currencyList[money2position].ForexBuying!! / currencyList[money2position].Unit!!.toDouble()) * money) / currencyList[money1position].ForexBuying!! / currencyList[money1position].Unit!!.toDouble()
                monayValueEditText1.setText(DecimalFormat("##.####").format(result).toString())
            }
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
            moneyValueSpinner2.onItemSelectedListener =
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
        CurrencyListSingleton.clearMemory()
    }

}