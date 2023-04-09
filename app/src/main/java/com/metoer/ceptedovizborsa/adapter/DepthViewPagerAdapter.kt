package com.metoer.ceptedovizborsa.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.metoer.ceptedovizborsa.view.activity.ChartActivity
import com.metoer.ceptedovizborsa.view.fragment.depth.HistoryFragment
import com.metoer.ceptedovizborsa.view.fragment.depth.OrdersFragment

class DepthViewPagerAdapter(
    chartActivity: ChartActivity,
    val baseSymbol: String,
    val quotetSymbol:String
) :
    FragmentStateAdapter(chartActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = OrdersFragment()
                val bundle = Bundle()
                bundle.putString("baseSymbol", baseSymbol)
                bundle.putString("quotetSymbol", quotetSymbol)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                HistoryFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}