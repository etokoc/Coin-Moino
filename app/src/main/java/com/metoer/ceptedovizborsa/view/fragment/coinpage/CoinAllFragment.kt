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
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CoinAllFragment : Fragment(), onItemClickListener {

    private var adapter = CoinAdapter(this)
    private var _binding: FragmentCoinPageBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CoinViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val coinPortfolioViewModel: CoinPortfolioViewModel by viewModels()
    private val coinList = ArrayList<CoinData>()
    private lateinit var coinListForPaging: PagingData<CoinData>

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
                sortList(it.second, it.first)
                binding.recylerview.scrollToPosition(0)
            }
        }
    }

    fun sortList(
        listSortType: FilterEnum,
        listSortItem: FilterEnum
    ) {
        val newList = SortListUtil()
       adapter.submitData(
            lifecycle = lifecycle, PagingData.from(
                newList.sortedForCoinList(
                    coinList, listSortType, listSortItem
                )
            )
        )
    }

    private fun initListener() {
        /**
         * Paging ile sayfalama
         */
        lifecycleScope.launch {
            viewModel.getAllCoinData.collectLatest { pagingData ->
                binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
                binding.recylerview.adapter = adapter
                coinListForPaging = pagingData
                adapter.submitData(pagingData)
            }
        }
        /**
         * paging ile alınan verilerin static olarak diğer coin sayfalarına aktarmak için [loadStateFlow] kullandık.
         * Çünkü adapter.submitData() asenkron bir işlem. Ne zaman adapter'e veri geldiğini ancak bu şekilde yakaladık.
         */
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    coinList.clear()
                    coinList.addAll(adapter.snapshot().items)
                }
        }

        sharedViewModel.coinList?.observe(viewLifecycleOwner) {
            if (it != null) {
                filter(it)
            }
        }
    }

    /**
     * [coinListForPaging] PagingData<CoinData> türündedir. Ve arama kutusu temizlendiğinde eski verilerin tekrar gelmesi için kullanılır.
     * Eğer aratılan bir kelime var ama sonucu yok ise [PagingData.empty] kullanılarak boş liste gösterilir kullanıcıya.
     */
    private fun filter(text: String) {
        if (text != "") {
            val filterlist: ArrayList<CoinData> = arrayListOf()
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
                adapter.submitData(lifecycle, PagingData.empty())
            } else {
                adapter.submitData(lifecycle, PagingData.from(filterlist))
            }
        } else {
            adapter.submitData(lifecycle, coinListForPaging)
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
        val coinData = coinList.get(position)
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