package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            val navController = findNavController(R.id.home_fragment)
            clockMenuBar.setupWithNavController(navController)
        }
    }

    override fun onResume() {
        super.onResume()

    }
}