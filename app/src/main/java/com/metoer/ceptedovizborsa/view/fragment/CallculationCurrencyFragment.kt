package com.metoer.ceptedovizborsa.view.fragment

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.CurrencySearchAdapter
import com.metoer.ceptedovizborsa.data.response.coin.rates.RatesData
import com.metoer.ceptedovizborsa.databinding.CustomSearchSpinnerBinding
import com.metoer.ceptedovizborsa.databinding.FragmentCallculationCurrencyBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.viewmodel.fragment.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_callculation_currency.*
import java.util.*


@AndroidEntryPoint
class CallculationCurrencyFragment : Fragment(), onItemClickListener {
    private val viewmodel: CurrencyViewModel by viewModels()
    private var _binding: FragmentCallculationCurrencyBinding? = null
    private var isFirst = false
    private var _dialog: Dialog? = null
    private var spinner1SelectedItem: RatesData? = null
    private var spinner2SelectedItem: RatesData? = null

    private val binding
        get() = _binding!!

    private var adapter = CurrencySearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallculationCurrencyBinding.inflate(inflater, container, false)
        binding.apply {
            if (savedInstanceState != null) {
                spinner1SelectedItem =
                    savedInstanceState.getParcelable(Constants.SPINNER1_STATE_KEY)
                spinner2SelectedItem =
                    savedInstanceState.getParcelable(Constants.SPINNER2_STATE_KEY)
            }
        }

        return binding.root
    }

    private var currencyList = ArrayList<RatesData>()
    override fun onResume() {
        super.onResume()
        viewmodel.getAllRatesData(requireContext().applicationContext)
        binding.apply {
            moneyValueEditText1.filters = editTextFilter()
            moneyValueEditText2.filters = editTextFilter()
            moneyValueEditText1.hint = requireContext().getString(R.string.para_1)
            moneyValueEditText2.hint = requireContext().getString(R.string.para_2)
            searchTextView1.text = spinner1SelectedItem?.id ?: getString(R.string.select_currency)
            searchTextView2.text = spinner2SelectedItem?.id ?: getString(R.string.select_currency)
        }
        initListener()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        viewmodel.ratesMutableList.observe(viewLifecycleOwner) {
            currencyList = it as ArrayList<RatesData>
            initSpinners(currencyList)
        }
        var money: Double
        binding.apply {
            var editControl = false
            var editControl2 = false
            moneyValueEditText1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty() && !moneyValueEditText1.text.toString()
                            .startsWith(',')
                    ) {
                        if (editControl && !editControl2 && spinner2SelectedItem != null && spinner1SelectedItem != null) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                money,
                                spinner1SelectedItem!!,
                                spinner2SelectedItem!!
                            )
                            binding.moneyValueEditText2.setText(moneyCalculate)
                        }
                    } else {
                        moneyValueEditText1.text.clear()
                        moneyValueEditText2.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    moneyValueEditText1.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                    editControl = false
                }

            })

            moneyValueEditText2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl2 = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty() && !moneyValueEditText2.text.toString()
                            .startsWith(',')
                    ) {
                        if (editControl2 && !editControl && spinner2SelectedItem != null && spinner1SelectedItem != null) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                money,
                                spinner2SelectedItem!!,
                                spinner1SelectedItem!!
                            )
                            binding.moneyValueEditText1.setText(moneyCalculate)
                        }
                    } else {
                        moneyValueEditText2.text.clear()
                        moneyValueEditText1.text.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    moneyValueEditText2.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                    editControl2 = false
                }

            })

        }

        adapter.setOnClickItemListener { item ->
            val header = item.id
            if (isFirst) {
                spinner1SelectedItem = item
                binding.searchTextView1.text = header
            } else {
                spinner2SelectedItem = item
                binding.searchTextView2.text = header
            }
            hideDialog()
        }

    }

    private fun initSpinners(currencyList: ArrayList<RatesData>) {
        binding.searchTextView1.setOnClickListener {
            isFirst = true
            searchDialog(binding.constraintLayout, currencyList)
        }
        binding.searchTextView2.setOnClickListener {
            isFirst = false
            searchDialog(binding.constraintLayout2, currencyList)
        }
    }

    private fun searchDialog(
        constraintLayout: ConstraintLayout,
        currencyList: ArrayList<RatesData>
    ) {
        val dialog = Dialog(requireContext())
        _dialog = dialog
        val bindingSearchDialog =
            CustomSearchSpinnerBinding.inflate(layoutInflater/*, binding.root, false*/)
        dialog.setContentView(bindingSearchDialog.root)
        val layoutParams = WindowManager.LayoutParams()
        val dialogVindow = dialog.window
        layoutParams.gravity = Gravity.TOP or Gravity.START or Gravity.END
        layoutParams.x = (constraintLayout.x + 50).toInt()
        layoutParams.y = (constraintLayout.y + constraintLayout.height - 40).toInt()
        dialogVindow?.attributes = layoutParams
        adapter.setData(currencyList)
        bindingSearchDialog.apply {
            searchSpinnerItem.currencySearchView.hint = getString(R.string.select_currency)
            searchSpinnerRecyclerView.adapter = adapter
            searchSpinnerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            initSearchBar(this)
        }
        dialogVindow?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        showDialog()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            Constants.SPINNER1_STATE_KEY,
            spinner1SelectedItem
        )
        outState.putParcelable(
            Constants.SPINNER2_STATE_KEY,
            spinner2SelectedItem
        )
    }

    override fun onItemClick(position: Int, parent: ViewGroup) {

    }

    private fun showDialog() {
        _dialog?.show()
    }

    private fun hideDialog() {
        _dialog?.hide()
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
        } else {
            adapter.filterList(filterlist)
        }
    }

    private fun initSearchBar(customSearchSpinnerBinding: CustomSearchSpinnerBinding) {
        binding.apply {
            customSearchSpinnerBinding.searchSpinnerItem.currencySearchView.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0.toString().isNotEmpty()) filter(p0.toString()) else filter(" ")
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            customSearchSpinnerBinding.searchSpinnerRecyclerView
        }
    }
}