package com.metoer.ceptedovizborsa.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.metoer.ceptedovizborsa.view.activity.ChartActivity
import com.metoer.ceptedovizborsa.view.fragment.depth.HistoryFragment
import com.metoer.ceptedovizborsa.view.fragment.depth.OrdersFragment

class DepthViewPagerAdapter(
    chartActivity: ChartActivity,
    val symbol: String
) :
    FragmentStateAdapter(chartActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                bundleData(OrdersFragment())
            }
            1 -> {
                bundleData(HistoryFragment())
            }
            else -> {
                Fragment()
            }
        }
    }

    private fun bundleData(fragment: Fragment): Fragment {
        val bundle = Bundle()
        bundle.putString("symbol", symbol)
        fragment.arguments = bundle
        return fragment
    }
}