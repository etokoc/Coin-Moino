package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CurrencyAdapter
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCurrencyBinding
import com.metoer.ceptedovizborsa.util.hide
import com.metoer.ceptedovizborsa.util.show
import com.metoer.ceptedovizborsa.viewmodel.fragment.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CurrencyFragment : Fragment(), OnClickListener {

    private var adapter = CurrencyAdapter(arrayListOf())
    private val viewModel: CurrencyViewModel by hiltNavGraphViewModels(R.id.my_navigation)
    private var _binding: FragmentCurrencyBinding? = null
    private var currencyList = ArrayList<Currency>()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        initListeners()
        binding.apply {

            currencySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {
                    if (searchText.isNotEmpty()) filter(searchText) else filter(" ")
                    return false
                }
            })
        }
        return binding.root
    }

    private fun filter(text: String) {
        val filterlist = ArrayList<Currency>()
        for (item in currencyList) {
            if (item.Isim?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
            ) {
                filterlist.add(item)
            }
        }
        if (filterlist.isEmpty()) {
            filterlist.clear()
            adapter.filterList(filterlist)
            binding.textviewEmptyMessage.show()
        } else {
            adapter.filterList(filterlist)
            binding.textviewEmptyMessage.hide()
        }
    }

    private fun initListeners() {
        viewModel.currencyMutableList.observe(viewLifecycleOwner) {
            currencyList.clear()
            adapter = CurrencyAdapter(it)
            currencyList.addAll(it)
            binding.apply {
                currencyRecyclerView.adapter = adapter
                currencyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                btnFilterName.setOnClickListener(this@CurrencyFragment)
                btnFilterValue.setOnClickListener(this@CurrencyFragment)
                btnFilterAmountIncrease.setOnClickListener(this@CurrencyFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val unixTime = System.currentTimeMillis()
        viewModel.getAllCurrencyData(unixTime.toString())
    }

    //OnclickListener for all views in layout
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_filter_name -> {
            }
            R.id.btn_filter_value -> {

            }
            R.id.btn_filter_amount_increase -> {
            }
        }
    }

}

