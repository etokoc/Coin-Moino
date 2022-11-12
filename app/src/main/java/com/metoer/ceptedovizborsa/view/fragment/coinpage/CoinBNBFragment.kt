package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinPageAdapter
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPageViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CoinBNBFragment : Fragment() {

    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private var adapter = CoinPageAdapter("BNB")
    private val viewModel: CoinPageViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }


    fun initListener() {
        viewModel.getAllMarketsCoinData("BNB").observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it)
            coinList.clear()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
        }

        sharedViewModel.coinList.observe(viewLifecycleOwner){
            filter(it)
        }
    }

    private val coinList = ArrayList<MarketData>()
    private fun filter(text: String) {
        val filterlist = ArrayList<MarketData>()
        for (item in coinList) {
            if (item.baseSymbol.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))!!
                || item.baseId.lowercase (Locale.getDefault())?.contains(text.lowercase(Locale.getDefault()))!!
            ) {
                filterlist.add(item)
            }
        }
        if (filterlist.isEmpty()) {
            filterlist.clear()
            adapter.filterList(filterlist)
        } else {
            adapter.filterList(filterlist)
        }
    }


}