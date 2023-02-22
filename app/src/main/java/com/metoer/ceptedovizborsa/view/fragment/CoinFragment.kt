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
import com.metoer.ceptedovizborsa.util.SearchViewUtil
import com.metoer.ceptedovizborsa.viewmodel.fragment.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoinFragment : Fragment() {

    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding!!
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

            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selectedFilterTab(tabItemList.size + 1)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            binding.tablayoutFilter.apply {
                val headerList = arrayListOf(
                    getString(R.string.ad),
                    getString(R.string.hacim),
                    getString(R.string.fiyat),
                    getString(R.string._24s_de_i_im)
                )
                headerList.forEach {
                    this.addTab(this.newTab())
                }
                headerList.forEachIndexed { index, s ->
                    val tabItem = ItemCoinTabBinding.inflate(LayoutInflater.from(requireContext()))
                    tabItem.tvTabItem.text = headerList[index]
                    tabItem.tvTabItem.textSize = 12f
                    tabItem.tvTabItem.setTextAppearance(R.style.SortItemTextAppearence)
                    val layout =
                        (this.getChildAt(0) as LinearLayout).getChildAt(3) as LinearLayout
                    val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 1.5f // e.g. 0.5f
                    layout.layoutParams = layoutParams
                    this.getTabAt(index)?.customView = tabItem.root
                    tabItem.imageView.tag = false
                    tabItemList.add(tabItem)
                }
                selectedFilterTab(tabItemList.size + 1)
                var statusType = FilterEnum.NAME
                var statusSortType: FilterEnum
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
                                if (tabItemList[tab!!.position].imageView.tag.toString()
                                        .toBoolean()
                                ) FilterEnum.ASC else {
                                    FilterEnum.DESC

                                }
                            sharedViewModel.filterStatus?.value =
                                Pair(
                                    statusType, statusSortType
                                )
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
                                if (tabItemList[tab!!.position].imageView.tag.toString()
                                        .toBoolean()
                                ) FilterEnum.ASC else FilterEnum.DESC
                            sharedViewModel.filterStatus?.value =
                                Pair(
                                    statusType, statusSortType
                                )
                        }
                    })
                }
            }
        }
    }

    private fun selectedFilterTab(position: Int) {
        tabItemList.forEachIndexed { index, tab ->
            if (index == position) {
                tab.imageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.transparent
                    )
                )
                if (tab.imageView.tag == true) {
                    tab.imageView.tag = false
                    tab.imageView.setImageResource(R.drawable.sortupicon)
                } else {
                    tab.imageView.tag = true
                    tab.imageView.setImageResource(R.drawable.sortdownicon)
                }
            } else {
                tab.imageView.tag = false
                tab.imageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.appGray
                    )
                )
            }
        }
    }

    private fun initSearchView() {
        binding.currencySearchView.currencySearchView.apply {
            addTextChangedListener {
                sharedViewModel.coinList?.value = it.toString()
                SearchViewUtil.searchViewTextChanged(binding.currencySearchView)
            }
            binding.currencySearchView.btnClear.setOnClickListener {
                SearchViewUtil.searcViewClearBtnClicked(binding.currencySearchView)
            }
            binding.currencySearchView.btnSearch.setOnClickListener {
                binding.currencySearchView.currencySearchView.requestFocus()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}