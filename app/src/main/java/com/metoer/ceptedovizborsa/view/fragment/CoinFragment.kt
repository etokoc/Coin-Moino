package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.ViewPagerAdapter
import com.metoer.ceptedovizborsa.data.response.currency.Currency
import com.metoer.ceptedovizborsa.databinding.FragmentCoinBinding
import com.metoer.ceptedovizborsa.view.fragment.coinpage.*
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class CoinFragment : Fragment() {

    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding!!
    //private var currencyList = ArrayList<Currency>()
    private val viewModel: CoinViewModel by viewModels()

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
    }

    private fun filter(text: String) {

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

    //Change Page Fragments (TRY,USDT,ETH, etc.)
    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.coin_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
    }
}