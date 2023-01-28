package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinPageAdapter
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPageViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CoinEthFragment : Fragment() {

    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private var adapter = CoinPageAdapter("ETH")
    private val viewModel: CoinPageViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private var webSocket: WebSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initWebSocket() {
        viewModel.apply {
            webSocket = getBinanceCoinWebSocket()
        }
    }

    override fun onResume() {
        super.onResume()
        initListener()
        initWebSocket()
    }


    fun initListener() {
        binding.recylerview.itemAnimator = null
        viewModel.getAllMarketsCoinData("ETH").observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it!! as ArrayList<MarketData>)
            coinList.clear()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
        }
        sharedViewModel.filterStatus.observe(viewLifecycleOwner) {
            adapter.sortList(it.second, it.first)
            binding.recylerview.scrollToPosition(0)
        }
        sharedViewModel.coinList.observe(viewLifecycleOwner) {
            filter(it)
        }
        connectWebSocket()
    }

    private fun connectWebSocket() {
        viewModel.getBinanceSocketListener().observe(viewLifecycleOwner) { webSocketData ->
            // TODO: Websocket Bağlantısı
            coinList.forEachIndexed { index, item ->
                if (item.baseId == webSocketData?.base && item.quoteId == webSocketData?.quote) {
                    val newList = adapter.updateData(webSocketData, index)
                    coinList = newList
                }
            }
        }
    }
    private var coinList = mutableListOf<MarketData>()
    private fun filter(text: String) {
        val filterlist = ArrayList<MarketData>()
        for (item in coinList) {
            if (item.baseSymbol?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true
                || item.baseId?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true
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

    override fun onPause() {
        webSocket?.cancel()
        super.onPause()
    }
    override fun onDestroy() {
        viewModel.clearBinanceSocketLiveData()
        webSocket?.cancel()
        super.onDestroy()
    }
}