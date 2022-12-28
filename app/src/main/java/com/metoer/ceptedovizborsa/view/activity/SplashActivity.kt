package com.metoer.ceptedovizborsa.view.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.metoer.ceptedovizborsa.databinding.ActivitySplashBinding
import com.metoer.ceptedovizborsa.util.FireBaseAnaliyticsUtil

class SplashActivity : AppCompatActivity() {
    var _binding: ActivitySplashBinding? = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        getDarkAndLightThema()
        FireBaseAnaliyticsUtil(applicationContext).startAnalytics()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.recreate()
    }

    private fun getDarkAndLightThema() {
        val prefs = getSharedPreferences("Thema", Activity.MODE_PRIVATE)
        val thema = prefs.getBoolean("night", false)
        if (thema) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onResume() {
        super.onResume()
    }


}