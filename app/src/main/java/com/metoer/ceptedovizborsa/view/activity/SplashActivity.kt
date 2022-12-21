package com.metoer.ceptedovizborsa.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.metoer.ceptedovizborsa.databinding.ActivitySplashBinding
import com.metoer.ceptedovizborsa.util.FirebaseUtil

class SplashActivity : AppCompatActivity() {
    var _binding : ActivitySplashBinding?  = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
    override fun onResume() {
        super.onResume()
    }
}