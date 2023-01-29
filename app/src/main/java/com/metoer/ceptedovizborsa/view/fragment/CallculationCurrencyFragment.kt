package com.metoer.ceptedovizborsa.view.fragment

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
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

    private var spinner1Position = 0
    private var spinner2Position = 0

    private val viewmodel: CurrencyViewModel by viewModels()
    private var _binding: FragmentCallculationCurrencyBinding? = null
    private val binding
        get() = _binding!!

    private var adapter = CurrencySearchAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallculationCurrencyBinding.inflate(inflater, container, false)
        binding.apply {
            if (savedInstanceState != null) {
                spinner1Position = savedInstanceState.getInt(Constants.SPINNER1_STATE_KEY, 0)
                spinner2Position = savedInstanceState.getInt(Constants.SPINNER2_STATE_KEY, 0)
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
                        if (editControl && !editControl2) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                currencyList,
                                money,
                                moneyValueSpinner1.selectedItemPosition,
                                moneyValueSpinner2.selectedItemPosition
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
                        if (editControl2 && !editControl) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val moneyCalculate = MoneyCalculateUtil.moneyConverter(
                                currencyList,
                                money,
                                moneyValueSpinner2.selectedItemPosition,
                                moneyValueSpinner1.selectedItemPosition
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

    }

    var isFirst = false
    private fun initSpinners(currencyList: ArrayList<RatesData>) {
        binding.searchTextView1.setOnClickListener {
            isFirst = true
            searchDialog(binding.searchTextView1, currencyList)
        }
        binding.searchTextView2.setOnClickListener {
            isFirst = false
            searchDialog(binding.searchTextView2, currencyList)
        }
        val arrayAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currencyList.map { currency -> currency.id })
        binding.moneyValueSpinner1.adapter = arrayAdapter
        binding.moneyValueSpinner2.adapter = arrayAdapter
        binding.moneyValueSpinner1.setSelection(spinner1Position)
        binding.moneyValueSpinner2.setSelection(spinner2Position)
    }

    var _dialog: Dialog? = null
    private fun searchDialog(textView: TextView, currencyList: ArrayList<RatesData>) {
        val dialog = Dialog(requireContext())
        _dialog = dialog
        val bindingSearchDialog =
            CustomSearchSpinnerBinding.inflate(layoutInflater/*, binding.root, false*/)
        dialog?.setContentView(bindingSearchDialog.root)
        val layoutParams = WindowManager.LayoutParams()
        val dialogVindow = dialog?.window
        layoutParams.gravity = Gravity.TOP or Gravity.START or Gravity.END
        layoutParams.x = (textView.x + 50).toInt()
        layoutParams.y = (textView.y + textView.height).toInt()
        dialogVindow?.attributes = layoutParams
        adapter.setData(currencyList)
        bindingSearchDialog.apply {
            searchSpinnerRecyclerView.adapter = adapter
            searchSpinnerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        dialogVindow?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        showDialog()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            Constants.SPINNER1_STATE_KEY,
            binding.moneyValueSpinner1.selectedItemPosition
        )
        outState.putInt(
            Constants.SPINNER2_STATE_KEY,
            binding.moneyValueSpinner2.selectedItemPosition
        )
    }

    override fun onItemClick(position: Int, parent: ViewGroup) {
        val header = currencyList[position].id
        if (isFirst) {
            binding.searchTextView1.text = header
        } else
            binding.searchTextView2.text = header
        hideDialog()
    }

    private fun showDialog() {
        _dialog?.show()
    }

    private fun hideDialog() {
        _dialog?.hide()
    }

}