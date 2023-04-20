package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPageViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket

@AndroidEntryPoint
class CoinBtcFragment : Fragment() {

    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    //private var adapter = CoinPageAdapter("BTC")
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

   /* private fun initWebSocket() {
        viewModel.apply {
            webSocket = getBinanceCoinWebSocket()
        }
    }

    override fun onResume() {
        super.onResume()
        initListener()
        initWebSocket()
    }

    private fun connectWebSocket() {
        viewModel.getBinanceSocketListener().observe(viewLifecycleOwner) { webSocketData ->
            // TODO: Websocket Bağlantısı
            coinList.forEachIndexed mForeach@{ index, item ->
                if (item.baseId == webSocketData?.base && item.quoteId == webSocketData?.quote) {
                    coinList = adapter.updateData(webSocketData, index)
                    return@mForeach
                }
            }
        }
    }

    fun initListener() {
        binding.recylerview.itemAnimator = null
        viewModel.getAllMarketsCoinData("BTC").observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it!! as ArrayList<PageTickerItem>)
            coinList.clear()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
            connectWebSocket()
        }

        sharedViewModel.filterStatus?.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.sortList(it.second, it.first)
                binding.recylerview.scrollToPosition(0)
            }
        }
        sharedViewModel.coinList?.observe(viewLifecycleOwner) {
            if (it != null) {
                filter(it)
            }
        }
    }

    private var coinList = mutableListOf<PageTickerItem>()
    private fun filter(text: String) {
        webSocket?.cancel()
        val filterlist = ArrayList<PageTickerItem>()
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
        if (text == ""){
            initWebSocket()
        }
    }

    override fun onPause() {
        webSocket?.cancel()
        sharedViewModel.coinList?.removeObservers(viewLifecycleOwner)
        sharedViewModel.filterStatus?.removeObservers(viewLifecycleOwner)
        viewModel.clearBinanceSocketLiveData()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.clearBinanceSocketLiveData()
        webSocket?.cancel()
        super.onDestroy()
    }*/

}