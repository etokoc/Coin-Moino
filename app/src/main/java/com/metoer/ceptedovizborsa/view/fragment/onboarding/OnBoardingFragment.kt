package com.metoer.ceptedovizborsa.view.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.OnBoardingAdapter
import com.metoer.ceptedovizborsa.databinding.FragmentOnBoardingBinding
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private var onBoardingAdapter: OnBoardingAdapter? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initViewPager()
    }

    private fun initViewPager() {
        val onBoardingList = arrayListOf(
            resources.getDrawable(R.drawable.onboarding1),
            resources.getDrawable(R.drawable.onboarding2),
            resources.getDrawable(R.drawable.onboarding3)
        )
        onBoardingAdapter?.setData(
            onBoardingList
        )
        binding.viewpagerOnboarding.adapter = onBoardingAdapter
        TabLayoutMediator(binding.tabDots, binding.viewpagerOnboarding){tab,position->
        }.attach()
    }

    private fun initAdapters() {
        onBoardingAdapter = OnBoardingAdapter()
    }

    override fun onResume() {
        super.onResume()
    }
}