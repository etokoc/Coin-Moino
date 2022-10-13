package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.IO) {
            val data =
                ApiNetworkAdapter.appApi().getData("1665690501196").body()!!.Currency?.get(0)
            Log.i("MYCAT", "onCreate: "+data!!.Isim +" "+data!!.ForexBuying)
        }

    }

    override fun onResume() {
        super.onResume()

    }
}