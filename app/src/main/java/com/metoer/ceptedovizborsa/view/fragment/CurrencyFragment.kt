package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CurrencyAdapter
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import com.metoer.ceptedovizborsa.databinding.FragmentCurrencyBinding
import com.metoer.ceptedovizborsa.util.ListSortEnum
import com.metoer.ceptedovizborsa.util.SearchViewUtil
import com.metoer.ceptedovizborsa.util.hide
import com.metoer.ceptedovizborsa.util.show
import com.metoer.ceptedovizborsa.viewmodel.fragment.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CurrencyFragment : Fragment(), OnClickListener {

    private var adapter = CurrencyAdapter()
    private val viewModel: CurrencyViewModel by hiltNavGraphViewModels(R.id.my_navigation)
    private var _binding: FragmentCurrencyBinding? = null
    val rotateDesc: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_descending
        )
    }
    val rotateAsc: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_ascending
        )
    }
    private var currencyList = ArrayList<RatesData>()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        initListeners()
        binding.apply {
            currencySearchView.currencySearchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) filter(p0.toString()) else filter(" ")
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
        return binding.root
    }

    private fun filter(text: String) {
        val filterlist = ArrayList<RatesData>()
        for (item in currencyList) {
            if (item.id?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault()))!!
            ) {
                filterlist.add(item)
            }
        }
        if (filterlist.isEmpty()) {
            filterlist.clear()
            adapter.filterList(filterlist)
            binding.textviewEmptyMessage.show()
        } else {
            adapter.filterList(filterlist)
            binding.textviewEmptyMessage.hide()
        }
    }

    private fun initListeners() {
        viewModel.ratesMutableList.observe(viewLifecycleOwner) {
            currencyList.clear()
            currencyList.addAll(it)
            adapter.setData(currencyList)
            binding.apply {
                currencyRecyclerView.adapter = adapter
                currencyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                btnFilterName.setOnClickListener(this@CurrencyFragment)
                btnFilterValue.setOnClickListener(this@CurrencyFragment)
                btnFilterAmountIncrease.setOnClickListener(this@CurrencyFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllRatesData(requireContext().applicationContext)
        initSearchView()
        binding.apply {
            iconName.tag = true
            iconValue.tag = true
            iconIncrase.tag = true
        }
    }

    private fun onAddButtonClicked(view: View) {
        setAnimation(view.tag.toString().toBoolean(), view)
    }

    //when button click false, and sort process for list.
    private fun setAnimation(clicked: Boolean, view: View) {
        if (clicked) {
            view.tag = false
            view.startAnimation(rotateDesc)
            adapter.sortList(ListSortEnum.DESC, sortListItem!!)
            binding.currencyRecyclerView.scrollToPosition(0)
        } else {
            view.tag = true
            view.startAnimation(rotateAsc)
            adapter.sortList(ListSortEnum.ASC, sortListItem!!)
            binding.currencyRecyclerView.scrollToPosition(0)
        }
    }

    var sortListItem: ListSortEnum? = null

    //OnclickListener for all views in layout
    override fun onClick(v: View?) {
        var icon: View? = null
        when (v?.id) {
            R.id.btn_filter_name -> {
                sortListItem = ListSortEnum.NAME
                binding.iconIncrase.setImageResource(R.drawable.minus)
                binding.iconValue.setImageResource(R.drawable.minus)
                binding.iconName.setImageResource(R.drawable.keyboard_shift)
                icon = binding.iconName
            }
            R.id.btn_filter_value -> {
                sortListItem = ListSortEnum.VALUE
                binding.iconIncrase.setImageResource(R.drawable.minus)
                binding.iconName.setImageResource(R.drawable.minus)
                binding.iconValue.setImageResource(R.drawable.keyboard_shift)
                icon = binding.iconValue
            }
            R.id.btn_filter_amount_increase -> {
                sortListItem = ListSortEnum.NAME
                adapter.sortList(ListSortEnum.DESC, ListSortEnum.NAME)
                binding.iconValue.setImageResource(R.drawable.minus)
                binding.iconName.setImageResource(R.drawable.minus)
                binding.iconIncrase.setImageResource(R.drawable.keyboard_shift)
                icon = binding.iconIncrase
            }
        }
        onAddButtonClicked(icon!!)
    }

    private fun initSearchView() {
        SearchViewUtil.searchViewController(binding.currencySearchView)
    }
}

