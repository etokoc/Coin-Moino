package com.metoer.ceptedovizborsa.view.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.metoer.ceptedovizborsa.util.SharedPrefencesUtil
import java.util.*

open class BaseActivity : AppCompatActivity() {
    fun supportLocale() {
        val prefs = SharedPrefencesUtil(applicationContext)
        val language = prefs.getLocal("My_Lang", String)
        val locale = Locale(language.toString())
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration, baseContext.resources.displayMetrics
        )
    }

}