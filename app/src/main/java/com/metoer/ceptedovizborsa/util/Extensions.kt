package com.metoer.ceptedovizborsa.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.metoer.ceptedovizborsa.R
import java.util.regex.Pattern

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

fun View.bacgroundColour(id: Int) {
    this.setBackgroundColor(ContextCompat.getColor(context, id))
}

fun getColorful(context: Context, id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun ImageView.setTintColor(@ColorRes color: Int) {
    this.setColorFilter(ContextCompat.getColor(context, color))
}

fun TextView.patternText(value: String?, pattern: String) {
    this.text = value?.let {
        NumberDecimalFormat.numberDecimalFormat(value = it, pattern)
    }
}

fun TextView.getStringPattern(context: Context, resId: Int, vararg formatArgs: Any) {
    this.text = context.getString(resId, formatArgs)
}

fun RadioButton.setDrawables(leftDrawable: Int? = null,topDrawable: Int? = null,rightDrawable: Int? = null,bottomDrawable: Int? = null) {
    val newLeftDrawable = leftDrawable?.let { ResourcesCompat.getDrawable(resources, it, null) }
    val newTopDrawable = topDrawable?.let { ResourcesCompat.getDrawable(resources, it, null) }
    val newRightDrawable = rightDrawable?.let { ResourcesCompat.getDrawable(resources, it, null) }
    val newBottomDrawable = bottomDrawable?.let { ResourcesCompat.getDrawable(resources, it, null) }
    this.setCompoundDrawablesWithIntrinsicBounds(newLeftDrawable,newTopDrawable,newRightDrawable,newBottomDrawable)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}
