package com.metoer.ceptedovizborsa.view.fragment.depth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.adapter.CoinDepthAdapter
import com.metoer.ceptedovizborsa.adapter.DepthEnum
import com.metoer.ceptedovizborsa.databinding.FragmentOrdersBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: OrderViewModel by viewModels()

    private lateinit var bidsAdapter: CoinDepthAdapter
    private lateinit var asksAdapter: CoinDepthAdapter

    lateinit var binanceDepthSocket: WebSocket


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        binding.apply {

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
        initlistener()
    }

    private fun initlistener() {
        val bundle = arguments
        val baseSymbol = bundle?.getString("baseSymbol")
        val quotetSymbol = bundle?.getString("quotetSymbol")
        baseSymbol?.let { base ->
            quotetSymbol?.let { quote ->
                binanceDepthSocket = viewModel.getBinanceDepthWebSocket(base, quote)
                viewModel.getBinanceSocketDepthListener()
                    ?.observe(this) { coinDepth ->
                        if (coinDepth != null) {
                            bidsAdapter.setData(coinDepth.bids)
                            asksAdapter.setData(coinDepth.asks)
                        }
                    }
            }
        }
    }

    private fun initAdapter() {
        asksAdapter = CoinDepthAdapter(DepthEnum.ASKS)
        bidsAdapter = CoinDepthAdapter(DepthEnum.BIDS)
        binding.apply {
            recyclerViewAsks.adapter = asksAdapter
            recyclerViewBids.adapter = bidsAdapter
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearBinanceSocketDepthLiveData()
        binanceDepthSocket.cancel()
    }
    override fun onDestroy() {
        viewModel.clearBinanceSocketDepthLiveData()
        binanceDepthSocket.cancel()
        super.onDestroy()
    }

}