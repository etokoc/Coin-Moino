package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metoer.ceptedovizborsa.data.ApiNetworkAdapter
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data =
            ApiNetworkAdapter.appApi().getData("1665690501196").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Toast.makeText(applicationContext, "" + it.Tarih, Toast.LENGTH_SHORT).show()
                }, {

                }).let {

                }
    }

    override fun onResume() {
        super.onResume()

    }
}