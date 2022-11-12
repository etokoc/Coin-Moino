package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.CoinAdapter
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.util.StaticCoinList
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinAllFragment : Fragment() {

    private var adapter = CoinAdapter()
    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CoinViewModel by viewModels()

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
    }

    fun initListener() {
        viewModel.getAllCoinData().observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it)
            binding.recylerview.adapter = adapter
            StaticCoinList.coinList = it
        }
    }
}