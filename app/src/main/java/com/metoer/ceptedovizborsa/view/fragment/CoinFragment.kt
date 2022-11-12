package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
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
    private val sharedViewModel : SharedViewModel by viewModels()

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
        binding.currencySearchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               sharedViewModel.coinList.value = newText.toString()
                return false
            }
        })
    }

}