package com.metoer.ceptedovizborsa.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import com.metoer.ceptedovizborsa.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    var _binding : ActivitySplashBinding?  = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
    }

    override fun onResume() {
        super.onResume()
    }
}