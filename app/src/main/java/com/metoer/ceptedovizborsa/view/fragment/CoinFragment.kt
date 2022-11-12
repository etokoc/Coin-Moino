package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.metoer.ceptedovizborsa.adapter.ViewPagerAdapter
import com.metoer.ceptedovizborsa.databinding.FragmentCoinBinding
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoinFragment : Fragment() {

    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CoinViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initTabLayout()
        initSearchView()
    }

    private fun initListeners() {

    }

    private fun initTabLayout() {
        binding.tabLayout.apply {
            val coinPageAdapter =
                ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
            binding.coinViewPager.adapter = coinPageAdapter
            TabLayoutMediator(this, binding.coinViewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "ALL"
                    }
                    1 -> {
                        tab.text = "USDT"

                    }
                    2 -> {
                        tab.text = "BNB"

                    }
                    3 -> {
                        tab.text = "BTC"

                    }
                    4 -> {
                        tab.text = "ETH"
                    }
                }
            }.attach()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initSearchView() {
        binding.currencySearchView.currencySearchView.apply {
            addTextChangedListener {
                sharedViewModel.coinList.value = it.toString()
                if (!this.text.isNullOrEmpty()) {
                    binding.currencySearchView.btnClear.visibility = View.VISIBLE
                } else
                    binding.currencySearchView.btnClear.visibility = View.GONE
            }
            binding.currencySearchView.btnClear.setOnClickListener {
                if (!this.text.isNullOrEmpty()) {
                    this.text?.clear()
                    this.clearFocus()
                } else {
                    binding.currencySearchView.btnClear.visibility = View.GONE
                    this.clearFocus()
                }
            }
            binding.currencySearchView.btnSearch.setOnClickListener {
                binding.currencySearchView.currencySearchView.requestFocus()
            }
        }
    }

}