package com.metoer.ceptedovizborsa.view.fragment.coinpage

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CoinAdapter
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.databinding.CustomCoinBuyDialogBinding
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPageBinding
import com.metoer.ceptedovizborsa.util.EditTextUtil
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.util.MoneyCalculateUtil
import com.metoer.ceptedovizborsa.util.StaticCoinList
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CoinAllFragment : Fragment(), CoinAdapter.onItemClickListener {

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
        sharedViewModel.filterStatus.observe(viewLifecycleOwner) {
            adapter.sortList(it.second, it.first)
            binding.recylerview.scrollToPosition(0)
        }
    }

    private fun initListener() {
        viewModel.getAllCoinData().observe(viewLifecycleOwner) {
            binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it)
            coinList.clear()
            coinList.addAll(it)
            binding.recylerview.adapter = adapter
            StaticCoinList.coinList = it
        }
        sharedViewModel.coinList.observe(viewLifecycleOwner) {
            filter(it)
        }
    }

    private val coinList = ArrayList<CoinData>()
    private fun filter(text: String) {
        val filterlist = ArrayList<CoinData>()
        for (item in coinList) {
            if (item.symbol?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
                || item.name?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
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
                val edittextCheck = EditTextUtil.editTextCheckControl(listOf(edittextCoinbuyDialogUnit,edittextCoinbuyDialogTotal))
                if (edittextCheck) {
                    buyCoin(coinData, edittextCoinbuyDialogUnit)
                } else {
                    //todo: burada dialog açıcaz
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
}