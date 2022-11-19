package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.ViewPagerAdapter
import com.metoer.ceptedovizborsa.databinding.FragmentCoinBinding
import com.metoer.ceptedovizborsa.databinding.ItemCoinTabBinding
import com.metoer.ceptedovizborsa.util.FilterEnum
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
    val tabItemList = ArrayList<ItemCoinTabBinding>()

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
                ViewPagerAdapter(childFragmentManager, lifecycle)
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

            binding.tablayoutFilter.apply {
                val headerList = arrayListOf("Ad", "Hacim", "Fiyat", "24s Değişim")
                headerList.forEachIndexed { index, s ->
                    val tabItem = ItemCoinTabBinding.inflate(LayoutInflater.from(requireContext()))
                    tabItem.tvTabItem.text = headerList[index]
                    val layout =
                        (this.getChildAt(0) as LinearLayout).getChildAt(3) as LinearLayout
                    val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 1.5f // e.g. 0.5f

                    layout.layoutParams = layoutParams
                    this.getTabAt(index)?.customView = tabItem.root
                    tabItem.imageView.tag = false
                    tabItemList.add(tabItem)
                }
                selectedFilterTab(tabItemList.size+1)
                var statusType = FilterEnum.NAME
                var statusSortType: FilterEnum
                val isClicked = arrayListOf(false, false, false, false)
                binding.tablayoutFilter.apply {
                    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            when (tab?.position) {
                                0 -> {
                                    statusType = FilterEnum.NAME
                                }
                                1 -> {
                                    statusType = FilterEnum.VOLUME

                                }
                                2 -> {
                                    statusType = FilterEnum.PRICE

                                }
                                3 -> {
                                    statusType = FilterEnum.HOUR24

                                }
                            }
                            selectedFilterTab(selectedTabPosition)
                            statusSortType =
                                if (isClicked[tab?.position!!]) FilterEnum.DESC else FilterEnum.ASC
                            sharedViewModel.filterStatus.value =
                                Pair(
                                    statusType, statusSortType
                                )
                            isClicked[tab.position] = !isClicked[tab.position]

                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {
                            when (tab?.position) {
                                0 -> {
                                    statusType = FilterEnum.NAME
                                }
                                1 -> {
                                    statusType = FilterEnum.VOLUME

                                }
                                2 -> {
                                    statusType = FilterEnum.PRICE

                                }
                                3 -> {
                                    statusType = FilterEnum.HOUR24

                                }
                            }
                            selectedFilterTab(selectedTabPosition)
                            statusSortType =
                                if (isClicked[tab?.position!!]) FilterEnum.DESC else FilterEnum.ASC
                            sharedViewModel.filterStatus.value =
                                Pair(
                                    statusType, statusSortType
                                )
                            isClicked[tab.position] = !isClicked[tab.position]
                        }
                    })
                }
            }
        }
    }

    private fun selectedFilterTab(position: Int) {
        tabItemList.forEachIndexed { index, tab ->
            if (index == position) {
                tab.imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.transparent))
                if (tab.imageView.tag == true) {
                    tab.imageView.tag = false
                    tab.imageView.setImageDrawable(resources.getDrawable(R.drawable.sortupicon))
                } else {
                    tab.imageView.tag = true
                    tab.imageView.setImageDrawable(resources.getDrawable(R.drawable.sortdownicon))
                }
            } else
                tab.imageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.appGray
                    )
                )
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