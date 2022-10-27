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
        viewModel.getAllCoinData().observe(viewLifecycleOwner) {
            binding.apply {
//                coinRecylerview.adapter = CoinAdapter(it)
//                coinRecylerview.layoutManager = LinearLayoutManager(requireContext())
            }
        }
//        viewModel.getAllMarketsCoinData("ETH").observe(viewLifecycleOwner) {
//            Log.i("COINCOIN", "" + it)
//        }
    }

    private fun initTabLayout() {
        binding.tabLayout.apply {
            addTab(binding.tabLayout.newTab().setText("TRY"))
            addTab(binding.tabLayout.newTab().setText("USDT"))
            addTab(binding.tabLayout.newTab().setText("BNB"))
            addTab(binding.tabLayout.newTab().setText("BTC"))
            addTab(binding.tabLayout.newTab().setText("ETH"))
            binding.tabLayout.getTabAt(0)?.select()
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: Tab?) {
                    when (tab?.position) {
                        0 -> {
                            changeFragment(CoinTryFragment())
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

                //Change Page Fragments (TRY,USDT,ETH, etc.)
                fun changeFragment (fragment: Fragment){
                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.coin_container,fragment)
                        .addToBackStack(null)
                        .commit()
                }

                override fun onTabUnselected(tab: Tab?) {
                }

                override fun onTabReselected(tab: Tab?) {
                }

            })
        }
    }

    override fun onResume() {
        super.onResume()
    }
}