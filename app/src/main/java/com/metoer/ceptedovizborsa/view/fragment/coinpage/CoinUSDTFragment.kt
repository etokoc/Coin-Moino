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
import com.metoer.ceptedovizborsa.util.Constants.WEBSOCKET_CLOSE_NORMAL
import com.metoer.ceptedovizborsa.util.PageTickerTypeEnum
import com.metoer.ceptedovizborsa.util.showToastShort
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
    private var adapter = CoinPageAdapter(PageTickerTypeEnum.USDT)
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
        initWebSocket()
        initListener()
    }

    private fun initWebSocket() {
        viewModel.apply {
            webSocket = getBinanceCoinWebSocket()
        }
    }


    fun initListener() {
        binding.recylerview.itemAnimator = null
        viewModel.getPageTickerData(PageTickerTypeEnum.USDT).observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it!! as ArrayList<CoinPageTickerItem>)
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
                coinList = adapter.updateData(webSocketData?.find { response ->
                    response.symbol == item.symbol
                }, index)
                return@mForeach
            }
        }
    }

    private var coinList = mutableListOf<CoinPageTickerItem>()
    private fun filter(text: String) {
        val filterlist = ArrayList<CoinPageTickerItem>()
        for (item in coinList) {
            if (item.symbol?.lowercase(Locale.getDefault())
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
        webSocket?.close(WEBSOCKET_CLOSE_NORMAL,"Kullanıcı tarafından kapatıldı")
        sharedViewModel.filterStatus?.removeObservers(viewLifecycleOwner)
        sharedViewModel.coinList?.removeObservers(viewLifecycleOwner)
        sharedViewModel.clearFilterStatusLiveData()
        sharedViewModel.clearCoinListLiveData()
        viewModel.clearBinanceSocketLiveData()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.clearBinanceSocketLiveData()
        super.onDestroy()
    }
}