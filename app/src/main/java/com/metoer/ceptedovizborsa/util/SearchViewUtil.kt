package com.metoer.ceptedovizborsa.util

import androidx.core.widget.addTextChangedListener
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.SearchLayoutBinding

object SearchViewUtil {
    fun searchViewController(searhLayoutBinding: SearchLayoutBinding) {
        searhLayoutBinding.currencySearchView.apply {
            hint = context.getString(R.string.search_currency_name)
            addTextChangedListener {
                searchViewTextChanged(searhLayoutBinding)
            }
            searhLayoutBinding.btnClear.setOnClickListener {
                searcViewClearBtnClicked(searhLayoutBinding)
            }
            searhLayoutBinding.btnSearch.setOnClickListener {
                searhLayoutBinding.currencySearchView.requestFocus()
            }
        }
    }

    fun searcViewClearBtnClicked(searhLayoutBinding: SearchLayoutBinding) {
        searhLayoutBinding.btnClear.setOnClickListener {
            searhLayoutBinding.currencySearchView.apply {
                if (!this.text.isNullOrEmpty()) {
                    this.text?.clear()
                    this.clearFocus()
                } else {
                    searhLayoutBinding.btnClear.hide()
                    this.clearFocus()
                }
            }
        }
    }

    fun searchViewTextChanged(searhLayoutBinding: SearchLayoutBinding) {
        searhLayoutBinding.currencySearchView.apply {
            if (!this.text.isNullOrEmpty()) {
                searhLayoutBinding.btnClear.show()
                searhLayoutBinding.btnSearch.setTintColor(R.color.primary_color)
            } else {
                searhLayoutBinding.btnClear.hide()
                searhLayoutBinding.btnSearch.setTintColor(R.color.sortgray)
                this.clearFocus()
            }
        }
    }
}