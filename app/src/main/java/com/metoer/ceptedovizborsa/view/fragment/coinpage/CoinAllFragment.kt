package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CoinAdapter
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.databinding.CustomCoinBuyDialogBinding
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinAllFragment : Fragment(), onItemClickListener {

    private var adapter = CoinAdapter(this)
    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CoinViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val coinPortfolioViewModel: CoinPortfolioViewModel by viewModels()
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
        sharedViewModel.filterStatus?.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.sortList(it.second, it.first)
                binding.recylerview.scrollToPosition(0)
            }
        }
    }

    private fun initListener() {
        lifecycleScope.launch {
            viewModel.getAllCoinData.collect {
                binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
//                coinList.clear()
////                coinList.addAll(it)
                binding.recylerview.adapter = adapter
                adapter.submitData(it)
                it.map {
                    Log.i("OMErLOG", "${it.name}: ")
                }
//                StaticCoinList.coinList = it
            }
        }
        sharedViewModel.coinList?.observe(viewLifecycleOwner) {
            if (it != null) {
                filter(it)
            }
        }
    }

    private val coinList = ArrayList<CoinData>()
    private fun filter(text: String) {
//        val filterlist:ArrayList<PagingData<CoinData>> = arrayListOf()
//        for (item in coinList) {
//            if (item.symbol?.lowercase(Locale.getDefault())
//                    ?.contains(text.lowercase(Locale.getDefault()))!!
//                || item.name?.lowercase(Locale.getDefault())
//                    ?.contains(text.lowercase(Locale.getDefault()))!!
//            ) {
//                filterlist.add(item)
//            }
//        }
//        if (filterlist.isEmpty()) {
//            filterlist.clear()
//            adapter.submitData(lifecycle, filterlist)
//        } else {
//            adapter.filterList(filterlist)
//        }
    }

    private fun showDialog(container: ViewGroup?, coinData: CoinData) {
        val dialog = Dialog(requireContext())
        val bindingDialog =
            CustomCoinBuyDialogBinding.inflate(
                LayoutInflater.from(container!!.context),
                container,
                false
            )
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val window = dialog.window
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        bindingDialog.apply {
            edittextCoinbuyDialogUnit.filters = editTextFilter()
            edittextCoinbuyDialogTotal.filters = editTextFilter()
            edittextCoinbuyDialogUnit.hint = getString(R.string.miktar, coinData.symbol)
            edittextCoinbuyDialogTotal.hint = getString(R.string.toplam, "USD")
            MoneyCalculateUtil.coinConverter(
                edittextCoinbuyDialogUnit,
                edittextCoinbuyDialogTotal,
                coinData.priceUsd.toString()
            )
            buttonCoinbuyDialog.setOnClickListener {
                val edittextCheck = EditTextUtil.editTextCheckControl(
                    listOf(edittextCoinbuyDialogUnit)
                )
                if (edittextCheck) {
                    buyCoin(coinData, edittextCoinbuyDialogUnit)
                    dialog.dismiss()
                    CustomDialogUtil(
                        requireContext(),
                        container,
                        false,
                        false,
                        true,
                        true
                    ).showDialog()
                } else {
                    requireContext().showToastShort(getString(R.string.check_inputs))
                }
            }
        }
        dialog.setCancelable(true)
        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    override fun onItemClick(position: Int, parent: ViewGroup) {
        val coinData = adapter.itemList[position]
        coinData.apply {
            showDialog(parent, coinData)
        }
    }

    private fun buyCoin(coinData: CoinData, editText: EditText) {
        var coinUnit: Double
        editText.let {
            coinUnit = MoneyCalculateUtil.doubleConverter(it.text.toString())
        }
        coinData.apply {
            val coinBuyItem = CoinBuyItem(
                symbol,
                "USD",
                id,
                coinUnit,
                priceUsd!!.toDouble(),
                System.currentTimeMillis()
            )
            coinPortfolioViewModel.upsertCoinBuyItem(coinBuyItem)
        }
    }

    override fun onPause() {
        super.onPause()
//        sharedViewModel.coinList = null
        sharedViewModel.coinList?.removeObservers(viewLifecycleOwner)
        sharedViewModel.filterStatus?.removeObservers(viewLifecycleOwner)
        sharedViewModel.clearFilterStatusLiveData()
        sharedViewModel.clearCoinListLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}