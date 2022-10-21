package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.adapter.StockAdapter
import com.metoer.ceptedovizborsa.data.response.stock.detail.HisseYuzeysel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {

    }

    var listSize = 0
    val hisseList = ArrayList<HisseYuzeysel>()
    override fun onResume() {
        super.onResume()
        viewModel.getStockGeneralData()
        viewModel.stockGeneralLiveData.observe(viewLifecycleOwner) {
            it.forEach { data ->
                viewModel.getStockDetailData(data.kod)
            }
            listSize = it.size
        }
        viewModel.stockDetailLiveData.observe(viewLifecycleOwner) {
            hisseList.add(it)
            if (listSize == hisseList.size) {
                binding.stockRecylerview.adapter = StockAdapter(hisseList)
                binding.stockRecylerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }

    }
}