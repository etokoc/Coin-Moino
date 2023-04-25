package com.metoer.ceptedovizborsa.view.fragment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CoinPortfolioAdapter
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.databinding.CustomPortfolioDetailDialogBinding
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPortfolioBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.viewmodel.activity.ChartViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPageViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinPortfolioFragment : Fragment(), onItemClickListener {

    private var _binding: FragmentCoinPortfolioBinding? = null
    private lateinit var settingAnimation: AnimationDrawable
    private lateinit var settingAnimationClose: AnimationDrawable
    private var adapter = CoinPortfolioAdapter(this)
    private val chartViewModel: ChartViewModel by viewModels()
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
            (customDialogUtil.getView() as CustomPortfolioDetailDialogBinding).apply {
                this.textViewPortfolioDialogCoinname.text = coinName
                val value = (currentValueOfCoin!! - coinTakedValue!!) * coinUnit!!
                this.textViewPortfolioDialogCoinprofit.text =
                    calculatePrice(value, coinSymbolQuote!!)
                this.textviewDescription.text =
                    getString(
                        R.string.coin_taked_value,
                        NumberDecimalFormat.numberDecimalFormat(
                            coinTakedValue.toString(),
                            "###,###,###,###.########"
                        ),
                        NumberDecimalFormat.numberDecimalFormat(
                            currentValueOfCoin.toString(),
                            "###,###,###,###.########"
                        )
                    )
                this.textViewPortfolioDialogCoinchange.text = calculatePercent(
                    coinTakedValue!!,
                    currentValueOfCoin,
                    this.textViewPortfolioDialogCoinchange
                )
                this.buttonDelete.setOnClickListener {
                    viewModel.delete(coinBuyItem)
                    coinBuyItemList.removeAt(position)
                    adapter.setData(coinBuyItemList)
                    customDialogUtil.dismiss()
                }
                customDialogUtil.showDialog()
            }
            customDialogUtil.setOnClickListener {
                customDialogUtil.dismiss()
            }
        }
    }

    private fun calculatePercent(
        firstPrice: Double,
        currentPrice: Float,
        textView: TextView
    ): String {
        val percent = ((currentPrice - firstPrice) / currentPrice) * 100
        percentBacgroundTint(percent, textView)
        val result = getString(
            R.string.coin_exchange_parcent_text,
            NumberDecimalFormat.numberDecimalFormat(percent.toString(), "0.##"),
            "%"
        )
        return result
    }

    private fun percentBacgroundTint(parcent: Double, textView: TextView) {
        if (parcent > 0) {
            textView.background.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.coinValueRise
                )
            )
        } else if (parcent < 0) {
            textView.background.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.coinValueDrop
                )
            )
        } else {
            textView.background.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.appGray
                )
            )
        }
    }

    fun calculatePrice(value: Double, priceQuote: String): String {
        val result = NumberDecimalFormat.numberDecimalFormat(
            value.toString(), "###,###,###,###.######"
        )
        return getString(R.string.coin_profit, result, priceQuote)
    }

    override fun onItemClick(position: Int, parent: ViewGroup) {
        val coinData = adapter.itemList[position]
        coinData.apply {
            if (this.coinSymbolQuote == "USD") {
                val currentValueOfCoin = 0f
                showDialog(
                    container = this@CoinPortfolioFragment.binding.root,
                    coinData,
                    position,
                    currentValueOfCoin
                )
            } else {
                chartViewModel.getTickerFromBinanceData(
                    coinSymbol + coinSymbolQuote,
                    "1d",
                    "MINI"
                ).observe(viewLifecycleOwner) { tickerData ->
                    tickerData?.let { it1 ->
                        val currentValueOfCoin = it1.lastPrice.toFloat()
                        showDialog(binding.root, coinData, position, currentValueOfCoin)
                        chartViewModel.removeObserver(viewLifecycleOwner)
                    }
                }
            }
        }
    }
}