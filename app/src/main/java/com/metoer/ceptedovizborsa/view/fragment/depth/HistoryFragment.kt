package com.metoer.ceptedovizborsa.view.fragment.depth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinTradeAdapter
import com.metoer.ceptedovizborsa.databinding.FragmentHistoryBinding
import com.metoer.ceptedovizborsa.util.Constants.WEBSOCKET_CLOSE_NORMAL
import com.metoer.ceptedovizborsa.viewmodel.fragment.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private var adapter = CoinTradeAdapter()

    private var binanceTradeSocket: WebSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        binding.apply {

        }
        return binding.root
    }

    override fun onResume() {
        initistener()
        super.onResume()
    }

    private fun initistener() {
        val bundle = arguments
        val symbol = bundle?.getString("symbol")
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        binding.recyclerViewHistory.layoutManager = layoutManager
        binanceTradeSocket = symbol?.let { viewModel.getBinanceTradeWebSocket(it) }
        viewModel.getBinanceSocketTradeListener()?.observe(this) { tradeData ->
            if (tradeData != null) {
                adapter.setData(tradeData)
                binding.recyclerViewHistory.adapter = adapter
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.clearBinanceSocketTradeLiveData()
        binanceTradeSocket?.close(WEBSOCKET_CLOSE_NORMAL,"History data websocket closed")
    }

    override fun onDestroy() {
        viewModel.clearBinanceSocketTradeLiveData()
        super.onDestroy()
    }
}