package com.metoer.ceptedovizborsa.view.activity

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.metoer.ceptedovizborsa.util.SharedPrefencesUtil
import java.util.*

open class BaseActivity : AppCompatActivity() {
    fun supportLocale() {
        val locale = Locale(getSharedLanguage())
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration, baseContext.resources.displayMetrics
        )
    }
    private fun getSharedLanguage(): String {
        val prefs = SharedPrefencesUtil(applicationContext)
        val language = prefs.getLocal("My_Lang", String)
        return language.toString()
    }
    fun supportThema(){
        val prefs = SharedPrefencesUtil(applicationContext)
        val thema = prefs.getLocal("night", Boolean)
        if (thema == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)

        val configuration = Configuration(newBase?.resources?.configuration)
        val fontScale = configuration.fontScale

        val prefs = SharedPrefencesUtil(applicationContext)
        val fontSize = prefs.getLocal("Font_Size", Float)

        if (fontSize == 0f){
            if (fontScale > 1.0f) {
                configuration.fontScale = 1.0f
                applyOverrideConfiguration(configuration)
            }
        }
        else{
            configuration.fontScale = fontSize as Float
            applyOverrideConfiguration(configuration)
        }
    }
}