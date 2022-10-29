package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.metoer.ceptedovizborsa.databinding.ActivityChartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartActivity : AppCompatActivity() {

    private var _binding: ActivityChartBinding? = null
    private val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}