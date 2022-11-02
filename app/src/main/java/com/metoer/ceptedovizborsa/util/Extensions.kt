package com.metoer.ceptedovizborsa.util

import android.content.Context
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invs() {
    this.visibility = View.INVISIBLE
}

fun EditText.setDefaultKeyListener(format: String) {
    this.keyListener = DigitsKeyListener.getInstance(format)
}

fun TextView.textColors(colorId: Int) {
    this.setTextColor(ContextCompat.getColor(context, colorId))
}

fun Context.showToastLong(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.showToastShort(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
fun View.bacgroundColour(id:Int){
    this.setBackgroundColor(ContextCompat.getColor(context,id))
}

