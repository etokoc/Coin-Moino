package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinAdapter
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.util.StaticCoinList
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CoinAllFragment : Fragment(), CoinAdapter.onItemClickListener {

    private var adapter = CoinAdapter(this)
    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CoinViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
        sharedViewModel.filterStatus.observe(viewLifecycleOwner) {
            adapter.sortList(it.second, it.first)
            binding.recylerview.scrollToPosition(0)
        }
    }

    fun initListener() {
        viewModel.getAllCoinData().observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it)
            coinList.clear()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
            StaticCoinList.coinList = it
        }
        sharedViewModel.coinList.observe(viewLifecycleOwner) {
            filter(it)
        }
    }

    private val coinList = ArrayList<CoinData>()
    private fun filter(text: String) {
        val filterlist = ArrayList<CoinData>()
        for (item in coinList) {
            if (item.symbol?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
                || item.name?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
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

    override fun onItemClick(position: Int) {
        val coinData = adapter.itemList[position]
        Toast.makeText(requireContext(), "" + coinData.symbol, Toast.LENGTH_SHORT).show()
    }
}