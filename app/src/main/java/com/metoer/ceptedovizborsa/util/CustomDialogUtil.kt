package com.metoer.ceptedovizborsa.util

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.CustomPortfolioDetailDialogBinding
import com.metoer.ceptedovizborsa.databinding.CustomUpdateAlertDialogBinding
import com.metoer.ceptedovizborsa.databinding.SuccessDialogBinding

class CustomDialogUtil(
    val context: Context,
    val container: ViewGroup,
    val attachToParent: Boolean = false,
    val forForcedUpdate: Boolean,
    val isSuccessDialog: Boolean,
    val setCancelable: Boolean = true
) {
    private var bindingDialog: ViewBinding? = null
    private var dialog: Dialog? = null

    init {
        if (forForcedUpdate)
            bindingDialog = CustomUpdateAlertDialogBinding.inflate(
                LayoutInflater.from(container.context), container, attachToParent
            ) else if (isSuccessDialog) {
            bindingDialog = SuccessDialogBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                attachToParent
            )
        } else
            bindingDialog = CustomPortfolioDetailDialogBinding.inflate(
                LayoutInflater.from(container.context), container, attachToParent
            )
        dialog = Dialog(context)
        dialog?.setContentView(bindingDialog!!.root)
        dialog?.setCancelable(setCancelable)
        val window = dialog?.window
        dialog!!.window?.setBackgroundDrawableResource(R.color.transparent)
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        initOnClicks()
    }

    private fun initOnClicks() {
        when (bindingDialog) {
            is CustomUpdateAlertDialogBinding -> {
                (bindingDialog as CustomUpdateAlertDialogBinding).buttonPortfolioDialogClose.setOnClickListener {
                    onClickListener.let {
                        it?.let { it1 -> it1() }
                    }
                }
            }
            is CustomPortfolioDetailDialogBinding -> {
                (bindingDialog as CustomPortfolioDetailDialogBinding).buttonPortfolioDialogClose.setOnClickListener {
                    onClickListener.let {
                        it?.let { it1 -> it1() }
                    }
                }
            }
        }
    }

    fun showDialog() {
        dialog?.show()
    }

    fun getView() = dialog

    private var onClickListener: (() -> Unit)? = null

    fun setOnClickListener(listener: () -> Unit) {
        onClickListener = listener
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}