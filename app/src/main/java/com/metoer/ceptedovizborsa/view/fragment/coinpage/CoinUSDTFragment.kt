package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinPageAdapter
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.util.PageTickerTypeEnum
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPageViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket
import java.util.*

@AndroidEntryPoint
class CoinUSDTFragment : Fragment() {

    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private var adapter = CoinPageAdapter("USDT")
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

    override fun onResume() {
        super.onResume()
        initListener()
        initWebSocket()
    }

    private fun initWebSocket() {
        viewModel.apply {
            webSocket = getBinanceCoinWebSocket()
        }
    }


    fun initListener() {
        binding.recylerview.itemAnimator = null
        viewModel.getPageTickerData(PageTickerTypeEnum.USDT).observe(viewLifecycleOwner) {
            it.forEach { item ->
                Log.i("OMERUSDT", " $item")
            }
        }
        viewModel.getAllMarketsCoinData("USDT").observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it!! as ArrayList<MarketData>)
            coinList = ArrayList()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
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
        connectWebSocket()
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

    private var coinList = mutableListOf<MarketData>()
    private fun filter(text: String) {
        webSocket?.cancel()
        //sorun var
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
        if (text == "") {
            initWebSocket()
        }
    }


    override fun onPause() {
        super.onPause()
        webSocket?.cancel()
        sharedViewModel.filterStatus?.removeObservers(viewLifecycleOwner)
        sharedViewModel.coinList?.removeObservers(viewLifecycleOwner)
        sharedViewModel.clearFilterStatusLiveData()
        sharedViewModel.clearCoinListLiveData()
        viewModel.clearBinanceSocketLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.cancel()
        viewModel.clearBinanceSocketLiveData()
    }
}