package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.FragmentCoinBinding
import com.metoer.ceptedovizborsa.view.fragment.coinpage.*
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoinFragment : Fragment() {

    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding!!
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

    private fun initListeners() {
    }

    private fun initTabLayout() {
        binding.tabLayout.apply {
            addTab(binding.tabLayout.newTab().setText("USD"))
            addTab(binding.tabLayout.newTab().setText("USDT"))
            addTab(binding.tabLayout.newTab().setText("BNB"))
            addTab(binding.tabLayout.newTab().setText("BTC"))
            addTab(binding.tabLayout.newTab().setText("ETH"))
            //first fragment start in try
            val firstPage = CoinAllFragment()
            changeFragment(firstPage)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: Tab?) {
                    when (tab?.position) {
                        0 -> {
                            changeFragment(firstPage)
                        }
                        1 -> {
                            changeFragment(CoinUSDTFragment())

                        }
                        2 -> {
                            changeFragment(CoinBNBFragment())

                        }
                        3 -> {
                            changeFragment(CoinBtcFragment())

                        }
                        4 -> {
                            changeFragment(CoinEthFragment())
                        }
                    }
                }

                override fun onTabUnselected(tab: Tab?) {
                }

                override fun onTabReselected(tab: Tab?) {
                }

            })
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