package com.metoer.ceptedovizborsa.view.fragment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CoinPortfolioAdapter
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.databinding.CustomPortfolioDetailDialogBinding
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPortfolioBinding
import com.metoer.ceptedovizborsa.util.CustomDialogUtil
import com.metoer.ceptedovizborsa.util.NumberDecimalFormat
import com.metoer.ceptedovizborsa.util.StaticCoinList
import com.metoer.ceptedovizborsa.util.onItemClickListener
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPageViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinPortfolioFragment : Fragment(), onItemClickListener {

    private var _binding: FragmentCoinPortfolioBinding? = null
    private lateinit var settingAnimation: AnimationDrawable
    private lateinit var settingAnimationClose: AnimationDrawable
    private var adapter = CoinPortfolioAdapter(this)
    private val coinMarketViewModel: CoinPageViewModel by viewModels()
    private val binding
        get() = _binding!!
    private var clicked = false
    private val viewModel: CoinPortfolioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinPortfolioBinding.inflate(layoutInflater, container, false)
        binding.apply {
            imageViewPortfolioSettings.apply {
                setBackgroundResource(R.drawable.setting_switch_animation_start)
            }
            imageViewPortfolioSettings.setOnClickListener {
                portfolioSettingClicked()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListeners()
    }

    private fun portfolioSettingClicked() {
        binding.apply {
            setAnimation(clicked)
            clicked = !clicked
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.imageViewPortfolioSettings.apply {
                setBackgroundResource(R.drawable.setting_switch_animation_start)
                settingAnimation = background as AnimationDrawable
                settingAnimation.start()
            }
        } else {
            binding.imageViewPortfolioSettings.apply {
                setBackgroundResource(R.drawable.setting_switch_animation_close)
                settingAnimationClose = background as AnimationDrawable
                settingAnimationClose.start()
            }
        }
    }

    var coinBuyItemList = ArrayList<CoinBuyItem>()
    private fun initListeners() {
        viewModel.gelAllCoinBuyData().observe(viewLifecycleOwner) {
            binding.recylerViewCoinPortfolio.layoutManager =
                LinearLayoutManager(requireContext())
            adapter.setData(it)
            coinBuyItemList.addAll(it)
            binding.recylerViewCoinPortfolio.adapter = adapter
        }
    }

    private fun showDialog(
        container: ViewGroup?,
        coinBuyItem: CoinBuyItem,
        position: Int,
        currentValueOfCoin: Float?
    ) {
        coinBuyItem.apply {
            val customDialogUtil =
                CustomDialogUtil(
                    requireContext(),
                    container!!,
                    forForcedUpdate = false,
                    isSuccessDialog = false
                )
            customDialogUtil.showDialog()
            (customDialogUtil.getView() as CustomPortfolioDetailDialogBinding).apply {
                this.textViewPortfolioDialogCoinname.text = coinName
                val value = (currentValueOfCoin!! - coinTakedValue!!) * coinUnit!!
                this.textViewPortfolioDialogCoinprofit.text = calculatePrice(value,coinSymbolQuote!!)
                this.textviewDescription.text =
                    getString(
                        R.string.coin_taked_value,
                        coinTakedValue,
                        currentValueOfCoin
                    )
                this.buttonDelete.setOnClickListener {
                    viewModel.delete(coinBuyItem)
                    coinBuyItemList.removeAt(position)
                    adapter.setData(coinBuyItemList)
                    customDialogUtil.dismiss()
                }
            }
            customDialogUtil.setOnClickListener {
                customDialogUtil.dismiss()
            }
        }
    }

    fun calculatePrice(value: Double,priceQuote:String): String {
        val result = NumberDecimalFormat.numberDecimalFormat(
            value.toString(), "###,###,###,###.######"
        )
        return getString(R.string.coin_profit,result,priceQuote)
    }

    override fun onItemClick(position: Int, parent: ViewGroup) {
        val coinData = adapter.itemList[position]
        coinData.apply {
            if (this.coinSymbolQuote == "USD") {
                val currentValueOfCoin =
                    StaticCoinList.coinList.find { it.symbol == coinData.coinSymbol }?.priceUsd?.toFloat()
                showDialog(parent, coinData, position, currentValueOfCoin)
            } else {
                this.coinSymbolQuote?.let {
                    coinMarketViewModel.getAllMarketsCoinData(it)
                        .observe(viewLifecycleOwner) { value ->
                            val currentValueOfCoin =
                                value?.find { it.baseSymbol == coinData.coinSymbol }?.priceQuote?.toFloat()
                            showDialog(parent, coinData, position, currentValueOfCoin)
                        }
                }
            }
        }
    }
}