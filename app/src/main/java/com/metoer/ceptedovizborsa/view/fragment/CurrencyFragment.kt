package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CurrencyAdapter
import com.metoer.ceptedovizborsa.data.response.Currency
import com.metoer.ceptedovizborsa.data.response.TarihDate
import com.metoer.ceptedovizborsa.databinding.FragmentCurrencyBinding
import com.metoer.ceptedovizborsa.util.showToastShort
import com.metoer.ceptedovizborsa.viewmodel.fragment.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CurrencyFragment : Fragment() {

    private lateinit var adapter: CurrencyAdapter
    private val viewModel: CurrencyViewModel by hiltNavGraphViewModels(R.id.my_navigation)
    private var _binding: FragmentCurrencyBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        initListeners()
        binding.apply {
            currencySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {
                    filter(searchText)
                    return false
                }
            })
        }
        return binding.root
    }

    private fun filter(text: String) {
        val filterlist = ArrayList<Currency>()
        viewModel.currencyMutableList.observe(viewLifecycleOwner) {
            for (item in it) {
                if (item.Isim?.toLowerCase()?.contains(text.lowercase(Locale.getDefault()))!!) {
                    filterlist.add(item)
                }
            }
        }
        if (filterlist.isEmpty()) {
            context?.showToastShort("Veri Bulunamadı")
        } else {
            adapter.filterList(filterlist)
        }
    }

    private fun initListeners() {
        viewModel.currencyMutableList.observe(viewLifecycleOwner) {
            adapter=CurrencyAdapter(it)
            binding.currencyRecyclerView.adapter = adapter
            binding.currencyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCurrencyData("1665773475685")
    }
}