package com.metoer.ceptedovizborsa.view.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import com.metoer.ceptedovizborsa.util.showToastShort
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            val navController = findNavController(R.id.home_fragment)
            bottomMenuBar.setupWithNavController(navController)
            setSupportActionBar(appBar)

            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                mainDrawerLayout,
                R.string.open,
                R.string.close
            )
            mainDrawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            mainNav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.setting_menu -> {

                    }
                    R.id.about_menu -> {

                    }
                }
                true
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.END)
            if (toggle.onOptionsItemSelected(item)) {
                return true
            }
        } else {
            binding.mainDrawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

    }
}