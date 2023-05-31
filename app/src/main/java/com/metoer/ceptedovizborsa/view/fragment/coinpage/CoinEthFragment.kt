package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinPageAdapter
import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinPageTickerItem
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.PageTickerTypeEnum
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
    private var adapter = CoinPageAdapter(PageTickerTypeEnum.ETH)
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
        initWebSocket()
        initListener()
    }


    private fun initListener() {
        binding.recylerview.itemAnimator = null
        viewModel.getPageTickerData(PageTickerTypeEnum.ETH).observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it!! as ArrayList<CoinPageTickerItem>)
            coinList = java.util.ArrayList()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
        }
        sharedViewModel.filterStatus?.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.sortList(it.second, it.first)
                binding.recylerview.scrollToPosition(0)
            }
        }
        sharedViewModel.listenCoinList()?.observe(viewLifecycleOwner) {
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
                adapter.updateData(webSocketData?.find { response ->
                    response.symbol == item.symbol
                }, index)
                return@mForeach
            }
        }
    }

    private var coinList = mutableListOf<CoinPageTickerItem>()
    private fun filter(text: String) {
        webSocket?.close(Constants.WEBSOCKET_CLOSE_NORMAL, "Kullanıcı tarafından kapatıldı")
        val filterlist = ArrayList<CoinPageTickerItem>()
        for (item in coinList) {
            if (item.symbol?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true
                || item.symbol?.lowercase(Locale.getDefault())
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
        if (text == "" && !viewModel.getWebsocketIsRunnig()) {
            initWebSocket()
        }
    }

    override fun onPause() {
        webSocket?.close(Constants.WEBSOCKET_CLOSE_NORMAL, "Kullanıcı tarafından kapatıldı")
        sharedViewModel.filterStatus?.removeObservers(viewLifecycleOwner)
        viewModel.clearBinanceSocketLiveData()
        sharedViewModel.clearCoinListLiveData()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.clearBinanceSocketLiveData()
        webSocket?.close(Constants.WEBSOCKET_CLOSE_NORMAL, "Kullanıcı tarafından kapatıldı")
        super.onDestroy()
    }
}