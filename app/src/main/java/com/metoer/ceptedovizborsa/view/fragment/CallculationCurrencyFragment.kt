package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.metoer.ceptedovizborsa.data.CurrencyListSingleton
import com.metoer.ceptedovizborsa.data.response.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCallculationCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CallculationCurrencyFragment : Fragment() {
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
        currencyList = CurrencyListSingleton.getList()
        initSpinners(currencyList)
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
                        binding.monayValueEditText1.setText(currencyList[i].ForexBuying.toString())
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
                        binding.monayValueEditText2.setText(currencyList[i].ForexBuying.toString())
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