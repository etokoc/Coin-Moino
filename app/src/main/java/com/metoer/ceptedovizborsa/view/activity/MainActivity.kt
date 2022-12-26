package com.metoer.ceptedovizborsa.view.activity

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import com.metoer.ceptedovizborsa.databinding.CustomLanguageDialogBinding
import com.metoer.ceptedovizborsa.util.showToastShort
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
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
                        languageDialog(binding.root)
                    }
                    R.id.about_menu -> {

                    }
                }
                true
            }
        }
    }

    private fun languageDialog(container: ViewGroup) {
        val dialog = Dialog(this)
        val bindingDialog =
            CustomLanguageDialogBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val window = dialog.window
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        bindingDialog.apply {
            if (loadLocaleString() == "tr" || loadLocaleString() == "") {
                radioButtonTurkish.isChecked = true
            } else {
                radioButtonEnglish.isChecked = true
            }
            buttonConfirmDialog.setOnClickListener {
                if (radioButtonTurkish.isChecked) {
                    showToastShort("Türkçe")
                    setLocale("tr")
                    recreate()
                } else {
                    showToastShort("İngilizce")
                    setLocale("en")
                    recreate()
                }
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
        val editor = getSharedPreferences("Language", MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    private fun loadLocaleString(): String {
        val prefs = getSharedPreferences("Language", Activity.MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "")
        return language!!
    }

    private fun loadLocale() {
        setLocale(loadLocaleString())
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