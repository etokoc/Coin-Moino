package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GlobalScope.launch(Dispatchers.IO) {
            val data =
                ApiNetworkAdapter.appApi().getData("1665690501196").body()!!.Currency?.get(0)
            Log.i("MYCAT", "onCreate: " + data!!.Isim + " " + data.ForexBuying)
        }

    }

    override fun onResume() {
        super.onResume()

    }
}