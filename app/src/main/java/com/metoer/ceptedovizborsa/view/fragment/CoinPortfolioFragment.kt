package com.metoer.ceptedovizborsa.view.fragment

import android.app.ActionBar
import android.app.Dialog
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CoinPortfolioAdapter
import com.metoer.ceptedovizborsa.databinding.CustomPortfolioDetailDialogBinding
import com.metoer.ceptedovizborsa.databinding.FragmentCoinPortfolioBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinPortfolioFragment : Fragment() {

    private var _binding: FragmentCoinPortfolioBinding? = null
    private lateinit var settingAnimation: AnimationDrawable
    private lateinit var settingAnimationClose: AnimationDrawable
    private var adapter = CoinPortfolioAdapter()
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

            textView3.setOnClickListener {
                val dialog = Dialog(requireContext())
                val bindingDialog =
                    CustomPortfolioDetailDialogBinding.inflate(
                        LayoutInflater.from(container!!.context),
                        container,
                        false
                    )
                dialog.setContentView(bindingDialog.root)
                dialog.window?.setBackgroundDrawableResource(R.color.transparent)
                val window = dialog.window
                window?.attributes!!.windowAnimations = R.style.DialogAnimation
                bindingDialog.apply {
                    val tvCoinName = textViewPortfolioDialogCoinname
                    val tvCoinProfit = textViewPortfolioDialogCoinprofit
                    val tvChange = textViewPortfolioDialogCoinchange
                    buttonPortfolioDialogClose.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                dialog.setCancelable(true)
                window.setLayout(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
                dialog.show()
            }

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


    private fun initListeners() {
        viewModel.gelAllCoinBuyData().observe(viewLifecycleOwner) {
            binding.recylerViewCoinPortfolio.layoutManager = LinearLayoutManager(requireContext())
            adapter.setData(it)
            binding.recylerViewCoinPortfolio.adapter = adapter
        }
    }
}