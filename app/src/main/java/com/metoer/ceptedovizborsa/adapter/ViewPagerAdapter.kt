package com.metoer.ceptedovizborsa.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.metoer.ceptedovizborsa.view.fragment.coinpage.*

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle:Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CoinAllFragment()
            }
            1 -> {
                CoinUSDTFragment()

            }
            2 -> {
                CoinBNBFragment()

            }
            3 -> {
                CoinBtcFragment()

            }
            4 -> {
                CoinEthFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}