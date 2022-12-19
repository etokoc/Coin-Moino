package com.metoer.ceptedovizborsa.view.fragment.onboarding

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.OnBoardingAdapter
import com.metoer.ceptedovizborsa.databinding.FragmentOnBoardingBinding
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.SharedPrefencesUtil
import com.metoer.ceptedovizborsa.util.invs
import com.metoer.ceptedovizborsa.util.show
import com.metoer.ceptedovizborsa.view.activity.MainActivity

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private var onBoardingAdapter: OnBoardingAdapter? = null
    val binding get() = _binding!!
    private var tabPosition = 0
    private lateinit var onBoardingList: ArrayList<Drawable>
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
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            btnNext.setOnClickListener {
                if (tabPosition < onBoardingList.size - 1) {
                    btnNext.text = "Sonraki"
                    tabPosition += 1
                    btnPrevius.show()
                    tabDots.selectTab(tabDots.getTabAt(tabPosition))
                }
                if (tabPosition > onBoardingList.size - 1) {
                    goToApp()
                }
                if (tabPosition == onBoardingList.size - 1) {
                    btnNext.text = "Uygulamaya Gir"
                    tabPosition += 1
                }
            }
            btnPrevius.setOnClickListener {
                if (tabPosition > 0) {
                    btnPrevius.show()
                    tabPosition -= 1
                    tabDots.selectTab(tabDots.getTabAt(tabPosition))
                }
                if (tabPosition < 0 || tabPosition == 0) {
                    btnPrevius.invs()
                }
            }
        }
    }

    //When next to the app
    private fun goToApp() {
        val local = SharedPrefencesUtil(requireContext())
        local.addLocal(Constants.IS_FIRST_OPEN_APP, true)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun initViewPager() {
        onBoardingList = arrayListOf(
            resources.getDrawable(R.drawable.onboarding1),
            resources.getDrawable(R.drawable.onboarding2),
            resources.getDrawable(R.drawable.onboarding3)
        )
        onBoardingAdapter?.setData(
            onBoardingList
        )
        binding.viewpagerOnboarding.adapter = onBoardingAdapter
        TabLayoutMediator(binding.tabDots, binding.viewpagerOnboarding) { tab, position ->
        }.attach()
    }

    private fun initAdapters() {
        onBoardingList = arrayListOf()
        onBoardingAdapter = OnBoardingAdapter()
    }

    override fun onResume() {
        super.onResume()
    }
}