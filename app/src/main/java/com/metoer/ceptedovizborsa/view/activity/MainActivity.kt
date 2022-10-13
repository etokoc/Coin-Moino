package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            ApiNetworkAdapter.appApi().getData("1665690501196")
        }
    }
}