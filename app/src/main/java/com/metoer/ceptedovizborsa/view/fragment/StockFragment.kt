package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.metoer.ceptedovizborsa.databinding.FragmentStockBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.StockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockFragment : Fragment() {

    private var _binding: FragmentStockBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: StockViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStockGeneralData()
        viewModel.stockGeneralLiveData.observe(viewLifecycleOwner) {
            viewModel.getStockDetailData(it.get(0).kod)
        }
        viewModel.stockDetailLiveData.observe(viewLifecycleOwner) {
            Log.i("dataDetail", "" + it)
        }
    }
}