package com.metoer.ceptedovizborsa.view.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.metoer.ceptedovizborsa.databinding.ActivitySplashBinding
import com.metoer.ceptedovizborsa.util.FireBaseAnaliyticsUtil

class SplashActivity : BaseActivity() {
    var _binding: ActivitySplashBinding? = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        getDarkAndLightThema()
        getLanguage()
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
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
        supportThema()
    }
    private fun getLanguage(){
        supportLocale()
    }

    override fun onResume() {
        super.onResume()
    }


}